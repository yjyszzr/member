package com.dl.member.service;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.model.UserWithdraw;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.UserAccountParam;
import com.dl.param.OrderSnParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DoubleValue;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserAccountMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.UserAccountCurMonthDTO;
import com.dl.member.dto.UserAccountDTO;
import com.dl.member.enums.MemberEnums;
import com.alibaba.fastjson.JSON;
import com.dl.api.IOrderService;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.dto.OrderDTO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    	String inPrams = JSON.toJSONString(surplusPayParam);
    	log.info(DateUtil.getCurrentDateTime()+"使用到了部分或全部余额时候支付传递的参数:"+inPrams);
    	
    	Integer userId = SessionUtil.getUserId();
        User user = userService.findById(userId);
        
        //用户余额
        BigDecimal yue = user.getUserMoney().add(user.getUserMoneyLimit());
        BigDecimal surplus = surplusPayParam.getMoneyPaid();
        if (yue.compareTo(surplus) == -1) {
            return ResultGenerator.genResult(MemberEnums.MONEY_IS_NOT_ENOUGH.getcode(), MemberEnums.MONEY_IS_NOT_ENOUGH.getMsg());
        }

        if (surplus.compareTo(BigDecimal.ZERO) == -1 || surplus.compareTo(BigDecimal.ZERO) == 0) {
            return ResultGenerator.genBadRequestResult("扣减余额不能为负数和0");
        }
        
        //校验
       // this.validMoneyMatchOrder(surplusPayParam);

        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = this.commonCalculateMoney(surplusPayParam.getSurplus(), ProjectConstant.BUY);
        
        UserAccountParam userAccountParam = new UserAccountParam();
        String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
        userAccountParam.setAccountSn(accountSn);
        Integer payType = surplusPayParam.getPayType();
        if(ProjectConstant.yuePay.equals(payType)) {
        	userAccountParam.setAmount(new BigDecimal(0).subtract(surplus));
        }else if(ProjectConstant.weixinPay.equals(payType) || ProjectConstant.aliPay.equals(payType)) {
        	userAccountParam.setAmount(new BigDecimal(0).subtract(surplusPayParam.getThirdPartPaid()));
        }else if(ProjectConstant.mixPay.equals(payType)) {
        	userAccountParam.setAmount(new BigDecimal(0).subtract(surplusPayParam.getMoneyPaid()));
        }
        userAccountParam.setCurBalance(surplusPaymentCallbackDTO.getCurBalance());
        userAccountParam.setAccountType(ProjectConstant.BUY);
        userAccountParam.setOrderSn(surplusPayParam.getOrderSn());
        userAccountParam.setPaymentName(surplusPayParam.getThirdPartName());
        userAccountParam.setThirdPartName(StringUtils.isEmpty(surplusPayParam.getThirdPartName())?"":surplusPayParam.getThirdPartName());
        userAccountParam.setThirdPartPaid(surplusPayParam.getThirdPartPaid() == null ?BigDecimal.ZERO:surplusPayParam.getThirdPartPaid());
        userAccountParam.setUserSurplus(surplusPaymentCallbackDTO.getUserSurplus());
        userAccountParam.setUserSurplusLimit(surplusPaymentCallbackDTO.getUserSurplusLimit());
        userAccountParam.setUserName(user.getUserName());
        userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
        if(ProjectConstant.yuePay.equals(payType)) {
        	userAccountParam.setStatus(Integer.valueOf(ProjectConstant.FINISH));
        }else {
        	userAccountParam.setStatus(Integer.valueOf(ProjectConstant.NOT_FINISH));
        }
        
        userAccountParam.setNote(this.createNote(surplusPayParam));
        userAccountParam.setPayId("");
        String accountSnRst = this.saveAccount(userAccountParam);
        if(StringUtils.isEmpty(accountSnRst)) {
        	surplusPaymentCallbackDTO.setAccountSn("");
        }else {
        	surplusPaymentCallbackDTO.setAccountSn(accountSnRst);
        }
        
        return ResultGenerator.genSuccessResult("余额支付后余额扣减成功",surplusPaymentCallbackDTO);
    }
  
    /**
     * 校验 1.钱是否与订单金额是否符合  2.是否重复扣款
     * @param surplusPayParam
     * @return
     */
    public BaseResult<SurplusPaymentCallbackDTO> validMoneyMatchOrder(SurplusPayParam surplusPayParam){
    	Integer payType = surplusPayParam.getPayType();
    	
        OrderSnParam orderSnParam = new OrderSnParam();
        orderSnParam.setOrderSn(surplusPayParam.getOrderSn());
        BaseResult<OrderDTO> orderDTORst = orderService.getOrderInfoByOrderSn(orderSnParam);
        if(orderDTORst.getCode() != 0) {
        	return ResultGenerator.genResult(orderDTORst.getCode(), orderDTORst.getMsg());
        }
        OrderDTO orderDTO = orderDTORst.getData();
        if(ProjectConstant.yuePay.equals(payType)) {
        	if(!surplusPayParam.getSurplus().equals(orderDTO.getSurplus())) {
        		throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
        	}
        	
        }else if(ProjectConstant.weixinPay.equals(payType) || ProjectConstant.aliPay.equals(payType)) {
        	if(!surplusPayParam.getThirdPartPaid().equals(orderDTO.getThirdPartyPaid())) {
        		throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
        	}
        }else if(ProjectConstant.mixPay.equals(payType)) {
        	if(!surplusPayParam.getSurplus().equals(orderDTO.getSurplus())) {
        		throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
        	}
        	if(!surplusPayParam.getThirdPartPaid().equals(orderDTO.getThirdPartyPaid())) {
        		throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
        	}
        }
        
        Condition c = new Condition(UserAccount.class);
        Criteria criteria = c.createCriteria();
        criteria.andCondition("order_sn =", surplusPayParam.getOrderSn());
        criteria.andCondition("user_id =", SessionUtil.getUserId());
        criteria.andCondition("amount =", "-".concat(surplusPayParam.getMoneyPaid().toString()));
        criteria.andCondition("process_type =", ProjectConstant.BUY);
        List<UserAccount> userAccountsList = this.findByCondition(c);
        if (userAccountsList.size() > 0) {
            throw new ServiceException(MemberEnums.USERACCOUNTS_ALREADY_REDUCE.getcode(), MemberEnums.USERACCOUNTS_ALREADY_REDUCE.getMsg());
        }
        
		return null;
    }
    /**
     * 记录详情
     * @param surplusPayParam
     * @return
     */
    public String createNote(SurplusPayParam surplusPayParam) {
    	String noteStr = "";
    	Integer payType = surplusPayParam.getPayType();
    	if(payType.equals(ProjectConstant.yuePay)) {
    		noteStr = "余额支付"+surplusPayParam.getSurplus()+"元";
    	}else if(payType.equals(ProjectConstant.aliPay) || payType.equals(ProjectConstant.weixinPay)) {
    		noteStr = surplusPayParam.getThirdPartName()+"支付"+surplusPayParam.getThirdPartPaid()+"元";
    	}else if(payType.equals(ProjectConstant.mixPay)) {
        	noteStr = surplusPayParam.getThirdPartName()+"支付"+surplusPayParam.getThirdPartPaid()+"元\n"
    		        +"余额支付"+surplusPayParam.getSurplus()+"元";
    	}
    	if(null != surplusPayParam.getBonusMoney()) {
    		noteStr = noteStr + "\n红包支付"+surplusPayParam.getBonusMoney()+"元";
    	}
		return noteStr;
    }
    /**
     * 更新用户账户信息
     * @param payId
     * @param status
     * @param accountSn
     * @return
     */
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
    
    /**
     * 统计当月的各个用途的资金和
     * @return
     */
    public BaseResult<UserAccountCurMonthDTO> countMoneyCurrentMonth() {
    	Integer userId = SessionUtil.getUserId();
    	UserAccountCurMonthDTO userAccountCurMonthDTO = new UserAccountCurMonthDTO();

    	List<UserAccount> userAccountList = userAccountMapper.queryUserAccountCurMonth(userId);
    	List<UserAccount> buyList = new ArrayList<>();
    	List<UserAccount> rechargeList = new ArrayList<>();
    	List<UserAccount> withdrawList = new ArrayList<>();
    	List<UserAccount> rewardList = new ArrayList<>();
 
    	for(UserAccount u:userAccountList) {
    		if(ProjectConstant.BUY.equals(u.getProcessType())) {
    			buyList.add(u);
    		}else if(ProjectConstant.RECHARGE.equals(u.getProcessType())) {
    			rechargeList.add(u);
    		}else if(ProjectConstant.WITHDRAW.equals(u.getProcessType())) {
    			withdrawList.add(u);
    		}else if(ProjectConstant.REWARD.equals(u.getProcessType())) {
    			rewardList.add(u);
    		}
    	}
    	
    	Double buyMoney = buyList.stream().map(s->s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
    	Double rechargeMoney = rechargeList.stream().map(s->s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
    	Double withdrawMoney = withdrawList.stream().map(s->s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
    	Double rewardMoney = rewardList.stream().map(s->s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
    	
    	userAccountCurMonthDTO.setBuyMoney(String.valueOf(0 - buyMoney));
    	userAccountCurMonthDTO.setRechargeMoney(String.valueOf(rechargeMoney));
    	userAccountCurMonthDTO.setWithDrawMoney(String.valueOf(withdrawMoney));
    	userAccountCurMonthDTO.setRewardMoney(String.valueOf(rewardMoney));
    	
    	return ResultGenerator.genSuccessResult("统计当月的各个用途的资金和成功",userAccountCurMonthDTO);
    }
    
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
    	String inPrams = JSON.toJSONString(surplusPayParam);
    	log.info(DateUtil.getCurrentDateTime()+"使用到了部分或全部余额时候回滚支付传递的参数:"+inPrams);
    	
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
        userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
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
        if(0 != processType) {
        	userAccount.setProcessType(processType);
        }
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
            userAccountDTO.setProcessType(String.valueOf(ua.getProcessType()));
            userAccountDTO.setProcessTypeChar(createProcessTypeString(ua.getProcessType()));
            String noteStr = ua.getNote();
            String firstNote =noteStr.substring(0,noteStr.indexOf("\n"));
            String lastNote = noteStr.substring(noteStr.indexOf("\n")+1);
            userAccountDTO.setProcessTypeName(firstNote);
            userAccountDTO.setNote(lastNote);
            String changeAmount = ua.getAmount().compareTo(BigDecimal.ZERO) == 1? "¥ " + "+" +ua.getAmount():"¥ " +String.valueOf(ua.getAmount());
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
    	BigDecimal curBalance = user.getUserMoney().add(user.getUserMoneyLimit()).subtract(userAccountParam.getAmount());
    	UserAccount userAccount = new UserAccount();
    	String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
    	userAccount.setAccountSn(accountSn);
    	userAccount.setAddTime(DateUtil.getCurrentTimeLong());
    	userAccount.setLastTime(userAccountParam.getLastTime());
    	userAccount.setAmount(userAccountParam.getAmount());
    	userAccount.setCurBalance(curBalance);
    	if(StringUtils.isNotEmpty(userAccountParam.getOrderSn())) {
    		userAccount.setOrderSn(userAccountParam.getOrderSn());
    	}
    	
    	userAccount.setPayId(userAccountParam.getPayId() == null?"":userAccountParam.getPayId());
    	userAccount.setPaymentName(userAccountParam.getPaymentName());
    	userAccount.setThirdPartName(StringUtils.isEmpty(userAccountParam.getThirdPartName())?"":userAccountParam.getThirdPartName());
    	userAccount.setUserSurplus(userAccountParam.getUserSurplus() == null ?BigDecimal.ZERO:userAccountParam.getUserSurplus());
    	userAccount.setUserSurplusLimit(userAccountParam.getUserSurplusLimit() == null ?BigDecimal.ZERO:userAccountParam.getUserSurplusLimit());
    	userAccount.setThirdPartPaid(userAccountParam.getThirdPartPaid() == null ? BigDecimal.ZERO:userAccountParam.getThirdPartPaid());
    	userAccount.setProcessType(userAccountParam.getAccountType());
    	userAccount.setUserId(user.getUserId());
    	userAccount.setStatus(userAccountParam.getStatus());
    	userAccount.setNote(userAccountParam.getNote());
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
