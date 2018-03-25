package com.dl.member.service;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.model.UserWithdraw;
import com.dl.member.param.UserAccountParam;
import com.dl.param.OrderSnParam;
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
import com.dl.dto.OrderDTO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
     * @param String orderSn,String surplus
     * @return
     * @see:余额支付引起账户变化 
     */
    @Transactional
    public BaseResult<SurplusPaymentCallbackDTO> addUserAccountByPay(String orderSn, BigDecimal surplus) {
    	Integer userId = SessionUtil.getUserId();
        User user = userService.findById(userId);
        
        //用户余额
        BigDecimal yue = user.getUserMoney().add(user.getUserMoneyLimit());
        if (yue.compareTo(surplus) == -1) {
            return ResultGenerator.genResult(MemberEnums.MONEY_IS_NOT_ENOUGH.getcode(), MemberEnums.MONEY_IS_NOT_ENOUGH.getMsg());
        }

        if (surplus.compareTo(BigDecimal.ZERO) == -1) {
            return ResultGenerator.genBadRequestResult("扣减余额不能为负数");
        }
        
        OrderSnParam orderSnParam = new OrderSnParam();
        orderSnParam.setOrderSn(orderSn);
        BaseResult<OrderDTO> orderRst = orderService.getOrderInfoByOrderSn(orderSnParam);
        if(orderRst.getCode() != 0) {
        	return ResultGenerator.genResult(orderRst.getCode(), orderRst.getMsg());
        }
        OrderDTO orderDTO = orderRst.getData();
        
        Condition c = new Condition(UserAccount.class);
        Criteria criteria = c.createCriteria();
        criteria.andCondition("order_sn =", orderSn);
        criteria.andCondition("user_id =", SessionUtil.getUserId());
        criteria.andCondition("amount =", "-".concat(surplus.toString()));
        criteria.andCondition("process_type =", ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND);
        List<UserAccount> userAccountsList = this.findByCondition(c);
        if (userAccountsList.size() > 0) {
            throw new ServiceException(MemberEnums.USERACCOUNTS_ALREADY_REDUCE.getcode(), MemberEnums.USERACCOUNTS_ALREADY_REDUCE.getMsg());
        }

        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = this.countReduceMoney(surplus, user);
                
        UserAccount uap = new UserAccount();
        String note = "";
        uap.setNote(note);
        uap.setUserId(userId);
        uap.setAdminUser(user.getUserName());
        uap.setAddTime(DateUtil.getCurrentTimeLong());
        String accountSn = this.createAccountSn(String.valueOf(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND));
        uap.setProcessType(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND);
        uap.setAccountSn(accountSn);
        uap.setAmount(new BigDecimal(0).subtract(surplus));
        uap.setCurBalance(surplusPaymentCallbackDTO.getCurBalance());
        uap.setPaymentCode("");
        uap.setPaymentName("");
        uap.setOrderSn(orderSn);
        uap.setParentSn(orderSn);
        this.save(uap);

        return ResultGenerator.genSuccessResult("余额支付后余额扣减成功",surplusPaymentCallbackDTO);
    }
    
    /**
     * 创建流水
     */
    public int saveUserAccount(UserAccountParam userAccountParam) {
    	Integer userId = SessionUtil.getUserId();
        UserAccount uap = new UserAccount();
        String note = "";
        uap.setNote(note);
        uap.setUserId(userId);
        uap.setAdminUser(userAccountParam.getUserName());
        uap.setAddTime(DateUtil.getCurrentTimeLong());
        String accountSn = this.createAccountSn(String.valueOf(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND));
        uap.setProcessType(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND);
        uap.setAccountSn(accountSn);
        uap.setAmount(userAccountParam.getAmount());
        uap.setCurBalance(userAccountParam.getCurBalance());
        uap.setPaymentCode(userAccountParam.getPaymentCode());
        uap.setPaymentName(userAccountParam.getPaymentName());
        uap.setOrderSn(userAccountParam.getOrderSn());
        uap.setParentSn("");
        this.save(uap);
		return userId;
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
     * 回滚余额支付的付款所用的钱
     * @param String orderSn,String surplus
     */
    @Transactional
    public SurplusPaymentCallbackDTO rollbackUserAccountChangeByPay(BigDecimal surplus,String orderSn) {
    	Integer userId= SessionUtil.getUserId();
    	User user = userService.findById(userId);
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
        
        UserAccount uap = new UserAccount();
        String note = "";
        uap.setNote(note);
        uap.setUserId(user.getUserId());
        uap.setAdminUser(user.getUserName());
        uap.setAddTime(DateUtil.getCurrentTimeLong());
        String accountSn = this.createAccountSn(String.valueOf(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND_ROLLBACK));
        uap.setProcessType(ProjectConstant.ACCOUNT_TYPE_TRADE_SURPLUS_SEND_ROLLBACK);
        uap.setAccountSn(accountSn);
        uap.setAmount(surplus);
        uap.setCurBalance(curBalance);
        uap.setPaymentCode("");
        uap.setPaymentName("");
        uap.setOrderSn(orderSn);
        uap.setParentSn("");
        this.save(uap);
        
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
        for(int i = 0;i < userAccountList.size();i++) {
        	UserAccount ua = userAccountList.get(i);
            UserAccountDTO userAccountDTO = new UserAccountDTO();
            userAccountDTO.setId(ua.getId());
            userAccountDTO.setAccountSn(ua.getAccountSn());
            userAccountDTO.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.date_sdf));
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
    public String saveAccount(BigDecimal amount,String orderSn,String paymentCode,String paymentName,Integer processType) {
    	User user = userService.findById(SessionUtil.getUserId());
    	BigDecimal curBalance = user.getUserMoney().add(user.getUserMoneyLimit()).add(user.getFrozenMoney()).subtract(amount);
    	UserAccount userAccount = new UserAccount();
    	String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
    	userAccount.setAccountSn(accountSn);
    	userAccount.setAddTime(DateUtil.getCurrentTimeLong());
    	userAccount.setAmount(amount);
    	userAccount.setCurBalance(curBalance);
    	userAccount.setOrderSn(orderSn);
    	userAccount.setPaymentCode(paymentCode);
    	userAccount.setPaymentName(paymentName);
    	userAccount.setProcessType(processType);
    	userAccount.setUserId(user.getUserId());
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
    
    
    /**
     * 生成流水号
     */
    public static String createAccountSn(String account_type) {
        String s1 = DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong() + Long.valueOf(8 * 3600), DateUtil.yyyyMMdd);
        String s2 = StringUtils.rightPad(account_type, 2, "0");
        int s3 = new Random().nextInt(900000) + 100000;
        return s1 + s2 + String.valueOf(s3);
    }

}
