package com.dl.member.service;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.model.UserWithdraw;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.UserAccountParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserAccountMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.UserAccountDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.api.IOrderService;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

@Service
@Slf4j
public class UserAccountService extends AbstractService<UserAccount> {
    @Resource
    private UserAccountMapper userAccountMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserService userService;
    
    @Resource
    private IOrderService orderService;
    
    @Resource
    private UserWithdrawService userWithdrawService;
    
    public static SNGenerator snGenerator = new SNGenerator();
    
    
    /**
     * @param SurplusPayParam surplusPayParam
     * @return
     * @see:余额支付引起账户变化 
     */
    @Transactional
    public BaseResult<SurplusPaymentCallbackDTO> addUserAccountByPay(SurplusPayParam surplusPayParam) {
    	Integer userId = SessionUtil.getUserId();
        User user = userService.findById(userId);
        
        //用户余额
        BigDecimal yue = user.getUserMoney().add(user.getUserMoneyLimit());
        BigDecimal surplus = surplusPayParam.getSurplus();
        if (yue.compareTo(surplus) == -1) {
            return ResultGenerator.genResult(MemberEnums.MONEY_IS_NOT_ENOUGH.getcode(), MemberEnums.MONEY_IS_NOT_ENOUGH.getMsg());
        }

        if (surplus.compareTo(BigDecimal.ZERO) == -1 || surplus.compareTo(BigDecimal.ZERO) == 0) {
            return ResultGenerator.genBadRequestResult("扣减余额不能为负数和0");
        }
        
        Condition c = new Condition(UserAccount.class);
        Criteria criteria = c.createCriteria();
        criteria.andCondition("order_sn =", surplusPayParam.getOrderSn());
        criteria.andCondition("user_id =", SessionUtil.getUserId());
        criteria.andCondition("amount =", "-".concat(surplus.toString()));
        criteria.andCondition("process_type =", ProjectConstant.BUY);
        List<UserAccount> userAccountsList = this.findByCondition(c);
        if (userAccountsList.size() > 0) {
            throw new ServiceException(MemberEnums.USERACCOUNTS_ALREADY_REDUCE.getcode(), MemberEnums.USERACCOUNTS_ALREADY_REDUCE.getMsg());
        }

        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = this.commonCalculateMoney(surplusPayParam.getSurplus(), ProjectConstant.BUY);
        
        UserAccountParam userAccountParam = new UserAccountParam();
        String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
        userAccountParam.setAccountSn(accountSn);
        userAccountParam.setAmount(new BigDecimal(0).subtract(surplus));
        userAccountParam.setCurBalance(surplusPaymentCallbackDTO.getCurBalance());
        userAccountParam.setAccountType(ProjectConstant.BUY);
        userAccountParam.setOrderSn(surplusPayParam.getOrderSn());
        userAccountParam.setPaymentName(surplusPayParam.getThirdPartName());
        userAccountParam.setThirdPartName(StringUtils.isEmpty(surplusPayParam.getThirdPartName())?"":surplusPayParam.getThirdPartName());
        userAccountParam.setThirdPartPaid(surplusPayParam.getThirdPartPaid() == null ?BigDecimal.ZERO:surplusPayParam.getThirdPartPaid());
        userAccountParam.setUserSurplus(surplusPaymentCallbackDTO.getUserSurplus());
        userAccountParam.setUserSurplusLimit(surplusPaymentCallbackDTO.getUserSurplusLimit());
        userAccountParam.setUserName(user.getUserName());
        if(ProjectConstant.yuePay.equals(surplusPayParam.getPayType())) {
        	userAccountParam.setStatus(Integer.valueOf(ProjectConstant.FINISH));
        }else {
        	userAccountParam.setStatus(Integer.valueOf(ProjectConstant.NOT_FINISH));
        }
        userAccountParam.setNote("");
        userAccountParam.setPayId("");
        String accountSnRst = this.saveAccount(userAccountParam);
        if(StringUtils.isEmpty(accountSnRst)) {
        	surplusPaymentCallbackDTO.setAccountSn("");
        }else {
        	surplusPaymentCallbackDTO.setAccountSn(accountSnRst);
        }
        
        return ResultGenerator.genSuccessResult("余额支付后余额扣减成功",surplusPaymentCallbackDTO);
    }
    
    public BaseResult<String> updateUserAccount(String payId,Integer status,String accountSn){
    	UserAccount userAccount = new UserAccount();
    	userAccount.setPayId(payId);
    	userAccount.setStatus(status);
    	userAccount.setAccountSn(accountSn);
    	int rst = userAccountMapper.updateUserAccountBySelective(userAccount);
    	if(1 != rst) {
    		return ResultGenerator.genFailResult("余额支付后余额扣减失败");
    	}
    	return ResultGenerator.genSuccessResult("余额支付后余额扣减成功","success");
    }
    
//    /**
//     * 创建账户流水
//     */
//    public int saveUserAccount(UserAccountParam userAccountParam) {
//    	Integer userId = SessionUtil.getUserId();
//        UserAccount uap = new UserAccount();
//        String note = "";
//        uap.setNote(note);
//        uap.setUserId(userId);
//        uap.setAdminUser(userAccountParam.getUserName());
//        uap.setAddTime(DateUtil.getCurrentTimeLong());
//        String accountSn = this.createAccountSn(String.valueOf(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND));
//        uap.setProcessType(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND);update
//        uap.setAccountSn(accountSn);
//        uap.setAmount(userAccountParam.getAmount());
//        uap.setCurBalance(userAccountParam.getCurBalance());
//        uap.setPaymentCode(userAccountParam.getPaymentCode());
//        uap.setPaymentName(userAccountParam.getPaymentName());
//        uap.setOrderSn(userAccountParam.getOrderSn());
//        uap.setParentSn("");
//        this.save(uap);
//		return userId;
//    }
    
    /**
     * @see 余额支付时候扣减余额和订单支出，优先使用可提现余额
     *      返回 可提现余额和不可提现余额 剩余的余额 各是多少 
     */
    @Transactional
    private SurplusPaymentCallbackDTO countReduceMoney(BigDecimal surplus, User user) {
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal user_money = BigDecimal.ZERO; //用户账户扣减过后的可提现余额
        BigDecimal user_money_limit = BigDecimal.ZERO;// 用户账户扣减过后的不可提现余额
        BigDecimal usedUserMoney = null;//订单使用的可提现余额
        BigDecimal usedUserMoneyLimit = null;// 订单使用的不可提现余额
        BigDecimal curBalance = BigDecimal.ZERO;//当前变动后的总余额
        money = user.getUserMoney().subtract(surplus);
        if (money.compareTo(BigDecimal.ZERO) >= 0) {//可提现余额 够	
            user_money = money;
            user_money_limit = user.getUserMoneyLimit();
        } else {//可提现余额 不够
            user_money = BigDecimal.ZERO;
            user_money_limit = user.getUserMoneyLimit().add(money);
        }
        curBalance = user_money_limit.add(user_money).add(user.getFrozenMoney());

        int moneyRst = userMapper.updateUserMoneyAndUserMoneyLimit(SessionUtil.getUserId(), user_money, user_money_limit);
        if(moneyRst != 1) {
        	log.error("更新用户账户资金异常");
        }
        
        //回写订单使用可提现和不可提现各自多少
        if (money.compareTo(BigDecimal.ZERO) >= 0) {//可提现余额 够
        	usedUserMoney = surplus;
        	usedUserMoneyLimit = BigDecimal.ZERO;
        } else {//可提现余额 不够
        	usedUserMoney = user.getUserMoney();
        	usedUserMoneyLimit = surplus.subtract(usedUserMoney);
        } 
        
        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = new SurplusPaymentCallbackDTO();
        surplusPaymentCallbackDTO.setSurplus(surplus);
        surplusPaymentCallbackDTO.setUserSurplus(usedUserMoney);
        surplusPaymentCallbackDTO.setUserSurplusLimit(usedUserMoneyLimit);
        surplusPaymentCallbackDTO.setCurBalance(curBalance);
        
        return surplusPaymentCallbackDTO;
    }
    
    /**
     * 账户公共计算服务
     * @param inOrOutMoney 非0
     * @param type:1-下单付款  2-充值  3-提现 4-退款
     * @return
     */
    public SurplusPaymentCallbackDTO commonCalculateMoney(BigDecimal inOrOutMoney,Integer type) {
    	BigDecimal money = BigDecimal.ZERO;
        BigDecimal user_money = BigDecimal.ZERO; //用户账户变动后的可提现余额
        BigDecimal user_money_limit = BigDecimal.ZERO;// 用户账户变动后的不可提现余额
        BigDecimal usedUserMoney = null;//使用的可提现余额
        BigDecimal usedUserMoneyLimit = null;//使用的不可提现余额
        BigDecimal curBalance = BigDecimal.ZERO;//当前变动后的总余额
        
    	Integer userId = SessionUtil.getUserId();
    	User user = userService.findById(userId);
    	if(ProjectConstant.BUY == type) {
            money = user.getUserMoney().subtract(inOrOutMoney);
            if (money.compareTo(BigDecimal.ZERO) >= 0) {//可提现余额 够	
                user_money = money;
                user_money_limit = user.getUserMoneyLimit();
            	usedUserMoney = inOrOutMoney;
            	usedUserMoneyLimit = BigDecimal.ZERO;
            } else {//可提现余额 不够
                user_money = BigDecimal.ZERO;
                user_money_limit = user.getUserMoneyLimit().add(money);
            	usedUserMoney = user.getUserMoney();
            	usedUserMoneyLimit = inOrOutMoney.subtract(usedUserMoney);
            }
            curBalance = user_money_limit.add(user_money);
    	}else if(ProjectConstant.RECHARGE == type) {
    		
    		user_money = user.getUserMoney();
    		user_money_limit = user.getUserMoneyLimit().add(inOrOutMoney);
    		curBalance = user_money_limit.add(user_money);
    		
    	}else if(ProjectConstant.WITHDRAW == type) {
    		
    		user_money = user.getUserMoney().subtract(inOrOutMoney);
    		user_money_limit = user.getUserMoneyLimit();
    		curBalance = user_money_limit.add(user_money);
    		
    	}else if(4 == type) {
    		
    	}
    	
        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = new SurplusPaymentCallbackDTO();
        surplusPaymentCallbackDTO.setSurplus(inOrOutMoney);
        surplusPaymentCallbackDTO.setUserSurplus(usedUserMoney);
        surplusPaymentCallbackDTO.setUserSurplusLimit(usedUserMoneyLimit);
        surplusPaymentCallbackDTO.setCurBalance(curBalance);
    	
    	return surplusPaymentCallbackDTO;
    	
    }
    
    /**
     * 回滚余额支付的付款所用的钱
     * @param String orderSn,String surplus
     */
    @Transactional
    public SurplusPaymentCallbackDTO rollbackUserAccountChangeByPay(SurplusPayParam surplusPayParam) {
    	Integer userId= SessionUtil.getUserId();
    	User user = userService.findById(userId);
    	BigDecimal surplus = surplusPayParam.getSurplus();
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal user_money = new BigDecimal(0); //用户账户剩下的可提现余额
        BigDecimal user_money_limit = new BigDecimal(0);// 用户账户剩下的不可提现余额
        money = surplus.subtract(user.getUserMoney());
        if (money.compareTo(BigDecimal.ZERO) <= 0) {//扣减的钱依然比当前账户的可用余额要小
        	user_money = user.getUserMoney().add(surplus);
        	user_money_limit = user.getUserMoneyLimit();
        } else {//当前账户的可用余额  不够 扣减的钱
        	user_money =  BigDecimal.ZERO;
        	user_money_limit = user.getUserMoneyLimit().add(money);
        }

        BigDecimal curBalance = user_money.add(user_money_limit);
        int moneyRst = userMapper.updateUserMoneyAndUserMoneyLimit(SessionUtil.getUserId(), user_money, user_money_limit);
        if(moneyRst != 1) {
        	log.error("回滚更新用户账户资金异常");
        }
        
        UserAccountParam userAccountParam = new UserAccountParam();
        String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
        userAccountParam.setAccountSn(accountSn);
        userAccountParam.setAmount(surplus);
        userAccountParam.setCurBalance(curBalance);
        userAccountParam.setAccountType(ProjectConstant.ACCOUNT_ROLLBACK);
        userAccountParam.setOrderSn(surplusPayParam.getOrderSn());
        userAccountParam.setPaymentName(surplusPayParam.getThirdPartName());
        userAccountParam.setThirdPartName(StringUtils.isEmpty(surplusPayParam.getThirdPartName())?"":surplusPayParam.getThirdPartName());
        userAccountParam.setThirdPartPaid(BigDecimal.ZERO);
        userAccountParam.setUserSurplus(BigDecimal.ZERO);
        userAccountParam.setUserSurplusLimit(BigDecimal.ZERO);
        userAccountParam.setUserName(user.getUserName());
        userAccountParam.setNote("");
        userAccountParam.setPayId("");
        this.saveAccount(userAccountParam);
        
        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = new SurplusPaymentCallbackDTO();
        surplusPaymentCallbackDTO.setSurplus(BigDecimal.ZERO);
        surplusPaymentCallbackDTO.setUserSurplus(BigDecimal.ZERO);
        surplusPaymentCallbackDTO.setUserSurplusLimit(BigDecimal.ZERO);
        
        return surplusPaymentCallbackDTO;
    }
    
 
    /**
     * 查询用户余额明细列表
     *
     * @return
     */
    public PageInfo<UserAccountDTO> getUserAccountList(Integer processType,Integer pageNum, Integer pageSize) {
        List<UserAccountDTO> userAccountListDTO = new ArrayList<>();
        Integer userId = SessionUtil.getUserId();
        
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userId);
        userAccount.setProcessType(processType);
        PageHelper.startPage(pageNum, pageSize);
        List<UserAccount> userAccountList = userAccountMapper.queryUserAccountBySelective(userAccount);
        if (userAccountList.size() == 0) {
            return new PageInfo<UserAccountDTO>(userAccountListDTO);
        }

        PageInfo<UserAccount> pageInfo = new PageInfo<UserAccount>(userAccountList);
        for(int i = 0,len = userAccountList.size();i < len;i++) {
        	UserAccount ua = userAccountList.get(i);
            UserAccountDTO userAccountDTO = new UserAccountDTO();
            userAccountDTO.setId(ua.getId());
            userAccountDTO.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.date_sdf));
            userAccountDTO.setAccountSn(ua.getAccountSn());
            userAccountDTO.setShotTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.short_time_sdf));
            userAccountDTO.setStatus(showStatus(ua.getProcessType(),ua.getId()));
            userAccountDTO.setProcessTypeName(createProcessTypeString(ua.getProcessType()));
            userAccountDTO.setNote(ua.getNote());
            String changeAmount = ua.getAmount().compareTo(BigDecimal.ZERO) == 1?"+" + ua.getAmount():String.valueOf(ua.getAmount());
            userAccountDTO.setChangeAmount(changeAmount);
            userAccountListDTO.add(userAccountDTO);
        }
        
        PageInfo<UserAccountDTO> result = new PageInfo<UserAccountDTO>();
        try {
			BeanUtils.copyProperties(result,pageInfo);
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
        
        result.setList(userAccountListDTO);
        return result;
    }    
    
    /**
     * 保存账户流水
     * @param amount
     * @param orderSn
     * @param paymentCode
     * @param paymentName
     * @param processType
     * @return
     */
    @Transactional
    public String saveAccount(UserAccountParam userAccountParam) {
    	User user = userService.findById(SessionUtil.getUserId());
    	BigDecimal curBalance = user.getUserMoney().add(user.getUserMoneyLimit()).add(user.getFrozenMoney()).subtract(userAccountParam.getAmount());
    	UserAccount userAccount = new UserAccount();
    	String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
    	userAccount.setAccountSn(accountSn);
    	userAccount.setAddTime(DateUtil.getCurrentTimeLong());
    	userAccount.setAmount(userAccountParam.getAmount());
    	userAccount.setCurBalance(curBalance);
    	if(StringUtils.isNotEmpty(userAccountParam.getOrderSn())) {
    		userAccount.setOrderSn(userAccountParam.getOrderSn());
    	}
    	
    	userAccount.setPayId(userAccountParam.getPayId() == null?"":userAccountParam.getPayId());
    	userAccount.setPaymentName(userAccountParam.getPaymentName());
    	userAccount.setThirdPartyName(StringUtils.isEmpty(userAccountParam.getThirdPartName())?"":userAccountParam.getThirdPartName());
    	userAccount.setUserSurplus(userAccountParam.getUserSurplus() == null ?BigDecimal.ZERO:userAccountParam.getUserSurplus());
    	userAccount.setUserSurplusLimit(userAccountParam.getUserSurplusLimit() == null ?BigDecimal.ZERO:userAccountParam.getUserSurplusLimit());
    	userAccount.setThirdPartPaid(userAccountParam.getThirdPartPaid() == null ? BigDecimal.ZERO:userAccountParam.getThirdPartPaid());
    	userAccount.setProcessType(userAccountParam.getAccountType());
    	userAccount.setUserId(user.getUserId());
    	userAccount.setStatus(userAccountParam.getStatus());
    	userAccount.setNote("");
    	userAccount.setParentSn("");
    	
    	int rst = userAccountMapper.insertUserAccount(userAccount);
    	if(rst != 1) {
    		log.error("生成流水账户失败");
    		return "";
    	}
    	
    	return accountSn;
    }
    
    /**
     * 构造提现状态
     * @param processType
     * @param accountId
     * @return
     */
    public String showStatus(Integer processType,Integer accountId) {
    	if(!ProjectConstant.WITHDRAW.equals(processType)) {
    		return "";
    	}
    	UserWithdraw userWithDraw = userWithdrawService.findBy("accountId", accountId);
    	String status = userWithDraw.getStatus();
		return status.equals(ProjectConstant.FINISH)?"状态:提现成功":"";
    }
    
    /**
     * 根据操作类型返回不同的文字
     * @param processType
     * @return
     */
    public String createProcessTypeString(Integer processType) {
    	String str = "";
    	switch(processType) {
	    	case 1:str = "奖";break;
	    	case 2:str = "充值";break;
	    	case 3:str = "购彩";break;
	    	case 4:str = "提现";break;
	    	case 5:str = "红包";break;
    	}
		return str;
    }
}
