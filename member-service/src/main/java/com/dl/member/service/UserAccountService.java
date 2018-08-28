package com.dl.member.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.alibaba.fastjson.JSON;
import com.dl.base.constant.CommonConstants;
import com.dl.base.enums.AccountEnum;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.LotteryWinningLogTempMapper;
import com.dl.member.dao.UserAccountMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserAccountByTimeDTO;
import com.dl.member.dto.UserAccountCurMonthDTO;
import com.dl.member.dto.UserAccountDTO;
import com.dl.member.dto.UserAccountListAndCountDTO;
import com.dl.member.dto.UserIdAndRewardDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.DlMessage;
import com.dl.member.model.LotteryWinningLogTemp;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.param.AmountTypeParam;
import com.dl.member.param.MemRollParam;
import com.dl.member.param.MemWithDrawSnParam;
import com.dl.member.param.RecharegeParam;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.UserAccountParam;
import com.dl.member.param.UserAccountParamByType;
import com.dl.member.param.WithDrawParam;
import com.dl.member.util.GeTuiMessage;
import com.dl.member.util.GeTuiUtil;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderDTO;
import com.dl.order.param.OrderSnListParam;
import com.dl.order.param.OrderSnParam;
import com.dl.order.param.UpdateOrderStatusByAnotherStatusParam;
import com.dl.shop.payment.api.IpaymentService;
import com.dl.shop.payment.dto.PayLogDetailDTO;
import com.dl.shop.payment.dto.UserWithdrawDetailDTO;
import com.dl.shop.payment.param.PayLogOrderSnParam;
import com.dl.shop.payment.param.WithDrawSnAndUserIdParam;
import com.dl.shop.payment.param.WithDrawSnParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;

@Service
@Slf4j
public class UserAccountService extends AbstractService<UserAccount> {
	@Resource
	private UserAccountMapper userAccountMapper;

	@Resource
	private UserMapper userMapper;

	@Resource
	private LotteryWinningLogTempMapper lotteryWinningLogTempMapper;

	@Resource
	private UserService userService;

	@Resource
	private IOrderService orderService;

	@Resource
	private IpaymentService payMentService;

	@Value("${spring.datasource.druid.url}")
	private String dbUrl;

	@Value("${spring.datasource.druid.username}")
	private String dbUserName;

	@Value("${spring.datasource.druid.password}")
	private String dbPass;

	@Value("${spring.datasource.druid.driver-class-name}")
	private String dbDriver;

	@Resource
	private SysConfigService sysConfigService;

	@Resource
	private DlMessageService userMessageService;

	@Resource
	private GeTuiUtil geTuiUtil;

	/**
	 * @param SurplusPayParam
	 *            surplusPayParam
	 * @return
	 * @see:包含了余额部分的扣款
	 */
	@Transactional
	public BaseResult<SurplusPaymentCallbackDTO> addUserAccountByPay(SurplusPayParam surplusPayParam) {
		String inPrams = JSON.toJSONString(surplusPayParam);
		log.info(DateUtil.getCurrentDateTime() + "使用到了部分或全部余额时候支付传递的参数:" + inPrams);
		Integer userId = SessionUtil.getUserId();
		User user = userMapper.selectUserFoUpdateByUserId(userId);//锁用户信息
		if (null == user) {
			log.error("扣减用户余额，未查到用户orderSn={},user_id={}",surplusPayParam.getOrderSn(),userId);
			return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "用户不存在，不能使用余额付款");
		}
		// 用户余额
		BigDecimal yue = user.getUserMoney().add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney());
		BigDecimal surplus = surplusPayParam.getSurplus();
		if (yue.compareTo(surplus) == -1) {
			log.error("扣减用户余额，余额不足orderSn={},user_id={}",surplusPayParam.getOrderSn(),userId);
			return ResultGenerator.genResult(MemberEnums.MONEY_IS_NOT_ENOUGH.getcode(), MemberEnums.MONEY_IS_NOT_ENOUGH.getMsg());
		}
		if (surplus.compareTo(BigDecimal.ZERO) < 0) {
			log.error("扣减用户余额，扣除余额小于0,orderSn={},user_id={},surplus={}",surplusPayParam.getOrderSn(),userId,surplus);
			return ResultGenerator.genResult(MemberEnums.MONEY_PAID_NOTLESS_ZERO.getcode(), MemberEnums.MONEY_PAID_NOTLESS_ZERO.getMsg());
		}
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal user_money = BigDecimal.ZERO; // 用户账户变动后的可提现余额
        BigDecimal user_money_limit = BigDecimal.ZERO;// 用户账户变动后的不可提现余额
        BigDecimal usedUserMoney = null;// 使用的可提现余额
        BigDecimal usedUserMoneyLimit = null;// 使用的不可提现余额
        BigDecimal curBalance = BigDecimal.ZERO;// 当前变动后的总余额
        BigDecimal frozenMoney = user.getFrozenMoney();// 冻结的资金
        User updateUser = new User();
        updateUser.setUserId(user.getUserId());
//        经测试锁生效了
//        if(Integer.valueOf(400328).equals(user.getUserId())){
//        	try {
//        		log.info("进入胡贺东用户测试看是否进行了锁住用户操作 begain");
//				Thread.sleep(15000);
//				log.info("进入胡贺东用户测试看是否进行了锁住用户操作 end");
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//        }
        money = user.getUserMoneyLimit().subtract(surplus);
        if (money.compareTo(BigDecimal.ZERO) >= 0) {// 不可提现余额 够
            user_money = user.getUserMoney();
            user_money_limit = money;
            usedUserMoney = BigDecimal.ZERO;
            usedUserMoneyLimit = surplus;
        } else {// 不可提现余额 不够
        	usedUserMoneyLimit = user.getUserMoneyLimit();
        	usedUserMoney = surplus.subtract(usedUserMoneyLimit);
        	user_money_limit = BigDecimal.ZERO;
            user_money = user.getUserMoney().subtract(usedUserMoney);
            updateUser.setUserMoney(user_money);
        }
        curBalance = user_money_limit.add(user_money);
        updateUser.setUserMoneyLimit(user_money_limit);
        userMapper.updateUserMoneyAndUserMoneyLimit(updateUser);
        SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = new SurplusPaymentCallbackDTO();
        surplusPaymentCallbackDTO.setSurplus(surplus);
        surplusPaymentCallbackDTO.setUserSurplus(usedUserMoney);
        surplusPaymentCallbackDTO.setUserSurplusLimit(usedUserMoneyLimit);
        surplusPaymentCallbackDTO.setCurBalance(curBalance);
        surplusPaymentCallbackDTO.setFrozenMoney(frozenMoney);
		UserAccount userAccountParam = new UserAccount();
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccountParam.setAccountSn(accountSn);
		userAccountParam.setAmount(BigDecimal.ZERO.subtract(surplusPayParam.getSurplus()));
		userAccountParam.setCurBalance(surplusPaymentCallbackDTO.getCurBalance());
		userAccountParam.setProcessType(ProjectConstant.BUY);
		userAccountParam.setUserId(userId);
		userAccountParam.setOrderSn(surplusPayParam.getOrderSn());
		userAccountParam.setThirdPartName(StringUtils.isEmpty(surplusPayParam.getThirdPartName()) ? "" : surplusPayParam.getThirdPartName());
		userAccountParam.setThirdPartPaid(surplusPayParam.getThirdPartPaid() == null ? BigDecimal.ZERO : surplusPayParam.getThirdPartPaid());
		userAccountParam.setUserSurplus(surplusPaymentCallbackDTO.getUserSurplus());
		userAccountParam.setUserSurplusLimit(surplusPaymentCallbackDTO.getUserSurplusLimit());
		userAccountParam.setBonusPrice(surplusPayParam.getBonusMoney());
		userAccountParam.setAddTime(DateUtil.getCurrentTimeLong());
		userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
		userAccountParam.setStatus(Integer.valueOf(ProjectConstant.FINISH));
		userAccountParam.setNote(this.createNote(surplusPayParam));
		userAccountParam.setPayId("");
		int insertRst = userAccountMapper.insertUserAccountBySelective(userAccountParam);
		if (1 == insertRst) {
			surplusPaymentCallbackDTO.setAccountSn(accountSn);
		} else {
			log.error("插入流水信息报错，未插入 流水信息={}",JSON.toJSONString(userAccountParam));
			surplusPaymentCallbackDTO.setAccountSn("");
		}

		return ResultGenerator.genSuccessResult("余额支付后余额扣减成功", surplusPaymentCallbackDTO);
	}

	/**
	 * 校验 1.钱是否与订单金额是否符合 2.是否重复扣款
	 * 
	 * @param surplusPayParam
	 * @return
	 */
	public BaseResult<SurplusPaymentCallbackDTO> validMoneyMatchOrder(SurplusPayParam surplusPayParam) {
		Integer payType = surplusPayParam.getPayType();

		OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(surplusPayParam.getOrderSn());
		BaseResult<OrderDTO> orderDTORst = orderService.getOrderInfoByOrderSn(orderSnParam);
		if (orderDTORst.getCode() != 0) {
			return ResultGenerator.genResult(orderDTORst.getCode(), orderDTORst.getMsg());
		}
		OrderDTO orderDTO = orderDTORst.getData();
		if (ProjectConstant.yuePay.equals(payType)) {
			if (!surplusPayParam.getSurplus().equals(orderDTO.getSurplus())) {
				throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
			}

		} else if (ProjectConstant.weixinPay.equals(payType) || ProjectConstant.aliPay.equals(payType)) {
			if (!surplusPayParam.getThirdPartPaid().equals(orderDTO.getThirdPartyPaid())) {
				throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
			}
		} else if (ProjectConstant.mixPay.equals(payType)) {
			if (!surplusPayParam.getSurplus().equals(orderDTO.getSurplus())) {
				throw new ServiceException(MemberEnums.ORDER_PARAM_NOT_MATCH.getcode(), MemberEnums.ORDER_PARAM_NOT_MATCH.getMsg());
			}
			if (!surplusPayParam.getThirdPartPaid().equals(orderDTO.getThirdPartyPaid())) {
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
	 * 
	 * @param surplusPayParam
	 * @return
	 */
	public String createNote(SurplusPayParam surplusPayParam) {
		String noteStr = "";
		if (null != surplusPayParam.getBonusMoney() && surplusPayParam.getBonusMoney().compareTo(BigDecimal.ZERO) == 1) {
			noteStr = noteStr + "红包支付" + surplusPayParam.getBonusMoney() + "元";
		}

		if (surplusPayParam.getThirdPartPaid().compareTo(BigDecimal.ZERO) > 0) {
			noteStr = surplusPayParam.getThirdPartName() + "支付" + surplusPayParam.getThirdPartPaid() + "元\n" + "余额支付" + surplusPayParam.getSurplus() + "元";
		}

		return noteStr;
	}

	/**
	 * 记录详情for thirdPay
	 * 
	 * @param surplusPayParam
	 * @return
	 */
	public String createNoteForThirdPay(UserAccountParamByType userAccountParamByType) {
		String noteStr = "";
		if (null != userAccountParamByType.getBonusPrice() && userAccountParamByType.getBonusPrice().compareTo(BigDecimal.ZERO) == 1) {
			noteStr = noteStr + "红包支付" + userAccountParamByType.getBonusPrice() + "元";
		}

		if (userAccountParamByType.getThirdPartPaid().compareTo(BigDecimal.ZERO) > 0) {
			noteStr = userAccountParamByType.getThirdPartName() + "支付" + userAccountParamByType.getThirdPartPaid() + "元\n";
		}

		return noteStr;
	}

	/**
	 * 更新用户账户信息
	 * 
	 * @param payId
	 * @param status
	 * @param accountSn
	 * @return
	 */
	public BaseResult<String> updateUserAccount(String payId, Integer status, String accountSn) {
		UserAccount userAccount = new UserAccount();
		userAccount.setPayId(payId);
		userAccount.setStatus(status);
		userAccount.setAccountSn(accountSn);
		int rst = userAccountMapper.updateUserAccountBySelective(userAccount);
		if (1 != rst) {
			return ResultGenerator.genFailResult("余额支付后余额扣减失败");
		}
		return ResultGenerator.genSuccessResult("余额支付后余额扣减成功", "success");
	}

	/**
	 * 查询用户余额明细列表
	 *
	 * @return
	 */
	public PageInfo<UserAccountDTO> getUserAccountList(Integer processType, Integer pageNum, Integer pageSize) {
		List<UserAccountDTO> userAccountListDTO = new ArrayList<>();
		Integer userId = SessionUtil.getUserId();

		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userId);
		if (0 != processType) {
			userAccount.setProcessType(processType);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<UserAccount> userAccountList = userAccountMapper.queryUserAccountBySelective(userAccount);
		if (userAccountList.size() == 0) {
			return new PageInfo<UserAccountDTO>(userAccountListDTO);
		}

		PageInfo<UserAccount> pageInfo = new PageInfo<UserAccount>(userAccountList);
		for (UserAccount ua : userAccountList) {
			UserAccountDTO userAccountDTO = new UserAccountDTO();
			userAccountDTO.setId(ua.getId());
			userAccountDTO.setPayId(ua.getPayId());
			userAccountDTO.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.date_sdf));
			userAccountDTO.setAccountSn(ua.getAccountSn());
			userAccountDTO.setShotTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.short_time_sdf));

			if (ua.getProcessType().equals(ProjectConstant.WITHDRAW)) {
				userAccountDTO.setStatus(showStatus(ua));
			}
			userAccountDTO.setStatus("");
			userAccountDTO.setProcessType(String.valueOf(ua.getProcessType()));
			userAccountDTO.setProcessTypeChar(AccountEnum.getShortStr(ua.getProcessType()));
			userAccountDTO.setProcessTypeName(AccountEnum.getName(ua.getProcessType()));
			userAccountDTO.setNote("");// 这个字段可以用数据库中的其他字段来拼，目前采用直接取的方式
			String changeAmount = ua.getAmount().compareTo(BigDecimal.ZERO) == 1 ? "+" + ua.getAmount() + "元" : String.valueOf(ua.getAmount() + "元");
			userAccountDTO.setChangeAmount(changeAmount);
			userAccountListDTO.add(userAccountDTO);
		}

		PageInfo<UserAccountDTO> result = new PageInfo<UserAccountDTO>();
		try {
			BeanUtils.copyProperties(result, pageInfo);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		result.setList(userAccountListDTO);
		return result;
	}

	/**
	 * 按照时间统计各个用途的资金和
	 * 
	 * @return
	 */
	public BaseResult<UserAccountByTimeDTO> countMoneyByTime(String timeType) {
		Integer userId = SessionUtil.getUserId();
		Integer startTime = 0;
		Integer endTime = 0;
		Date todayDate = new Date();
		List<UserAccount> userAccountList = null;
		UserAccountByTimeDTO userAccountByTimeDTO = new UserAccountByTimeDTO();
		List<UserAccount> buyList = new ArrayList<>();
		List<UserAccount> rechargeList = new ArrayList<>();
		List<UserAccount> withdrawList = new ArrayList<>();
		List<UserAccount> rewardList = new ArrayList<>();

		if (timeType.equals("0")) {
			UserAccount userAccount = new UserAccount();
			userAccount.setUserId(userId);
			userAccountList = userAccountMapper.queryUserAccountBySelective(userAccount);
		} else if (timeType.equals("1")) {
			startTime = DateUtil.getTimeAfterDays(todayDate, 1, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
		} else if (timeType.equals("2")) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -6, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 0, 0, 0);
		} else if (timeType.equals("3")) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -29, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 0, 0, 0);
		} else if (timeType.equals("4")) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -89, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 0, 0, 0);
		}

		userAccountList = userAccountMapper.countUserAccountByTime(startTime, endTime);
		if (userAccountList.size() == 0) {
			return ResultGenerator.genSuccessResult("按照时间统计各个用途的资金和", userAccountByTimeDTO);
		}

		for (UserAccount u : userAccountList) {
			if (ProjectConstant.BUY.equals(u.getProcessType())) {
				buyList.add(u);
			} else if (ProjectConstant.RECHARGE.equals(u.getProcessType())) {
				rechargeList.add(u);
			} else if (ProjectConstant.WITHDRAW.equals(u.getProcessType())) {
				withdrawList.add(u);
			} else if (ProjectConstant.REWARD.equals(u.getProcessType())) {
				rewardList.add(u);
			}
		}

		// 抵消出票失败的退款 和 提现失败的退款
		BigDecimal backBuyMoney = BigDecimal.ZERO;
		BigDecimal backWithDrawMoney = BigDecimal.ZERO;
		List<String> backBuyOrderSns = buyList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		List<String> backWithDrawOrderSns = withdrawList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		if (backBuyOrderSns.size() > 0) {
			backBuyMoney = userAccountMapper.countBackMoneyByProcessTyepByOrderSns(backBuyOrderSns, userId);
			if (null == backBuyMoney) {
				backBuyMoney = BigDecimal.ZERO;
			}
		}
		if (backWithDrawOrderSns.size() > 0) {
			backWithDrawMoney = userAccountMapper.countBackMoneyByProcessTyepByOrderSns(backWithDrawOrderSns, userId);
			if (null == backWithDrawMoney) {
				backWithDrawMoney = BigDecimal.ZERO;
			}
		}

		DecimalFormat df = new DecimalFormat("0.00");// 保留两位小数
		Double buyMoney = buyList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double totalBuyMoney = buyMoney + backBuyMoney.doubleValue();
		Double rechargeMoney = rechargeList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double withdrawMoney = withdrawList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double totalWithDrawMoney = withdrawMoney + backWithDrawMoney.doubleValue();
		Double rewardMoney = rewardList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		userAccountByTimeDTO.setBuyMoney(String.valueOf(df.format(0 - totalBuyMoney)));
		userAccountByTimeDTO.setRechargeMoney(String.valueOf(df.format(rechargeMoney)));
		userAccountByTimeDTO.setWithDrawMoney(String.valueOf(df.format(0 - totalWithDrawMoney)));
		userAccountByTimeDTO.setRewardMoney(String.valueOf(df.format(rewardMoney)));

		return ResultGenerator.genSuccessResult("统计当月的各个用途的资金和成功", userAccountByTimeDTO);
	}

	/**
	 * 查询账户列表和统计账户总和
	 * 
	 * @param amountTypeParam
	 * @return
	 */
	public BaseResult<UserAccountListAndCountDTO> getUserAccountListAndCountTotal(AmountTypeParam amountTypeParam) {
		UserAccountListAndCountDTO uDTO = new UserAccountListAndCountDTO();
		UserAccountByTimeDTO userAccountByTimeDTO = new UserAccountByTimeDTO();
				//this.createUserAccountTotal(amountTypeParam.getTimeType());
		PageInfo<UserAccountDTO> pageAccounts = this.queryUserAccountList(amountTypeParam);
		uDTO.setPageInfo(pageAccounts);
		uDTO.setUserAccountByTimeDTO(userAccountByTimeDTO);

		return ResultGenerator.genSuccessResult("success", uDTO);
	}

	public PageInfo<UserAccountDTO> queryUserAccountList(AmountTypeParam amountTypeParam) {
		Integer startTime = 0;
		Integer endTime = 0;
		Integer userId = SessionUtil.getUserId();
		Integer processType = Integer.valueOf(amountTypeParam.getAmountType());
		String timeType = amountTypeParam.getTimeType();
		Integer pageNum = Integer.valueOf(amountTypeParam.getPageNum());
		Integer pageSize = Integer.valueOf(amountTypeParam.getPageSize());
		Date todayDate = new Date();
		List<UserAccount> userAccountList = null;
		List<UserAccountDTO> userAccountListDTO = new ArrayList<>();

		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userId);
		if (0 != processType) {
			userAccount.setProcessType(processType);
		} else {
			processType = null;
		}
		PageHelper.startPage(pageNum, pageSize);
		if (ProjectConstant.ALL_TIME.equals(timeType)) {
			userAccountList = userAccountMapper.queryUserAccountBySelective(userAccount);
		} else if (ProjectConstant.TODAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, 1, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountList = userAccountMapper.queryUserAccountByTime(userId, processType, startTime, endTime);
		} else if (ProjectConstant.BEFORE_6_DAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -5, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountList = userAccountMapper.queryUserAccountByTime(userId, processType, startTime, endTime);
		} else if (ProjectConstant.BEFORE_29_DAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -28, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountList = userAccountMapper.queryUserAccountByTime(userId, processType, startTime, endTime);
		} else if (ProjectConstant.BEFORE_89_DAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -88, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountList = userAccountMapper.queryUserAccountByTime(userId, processType, startTime, endTime);
		}

		PageInfo<UserAccount> pageInfo = new PageInfo<UserAccount>(userAccountList);
		for (UserAccount ua : userAccountList) {
			UserAccountDTO userAccountDTO = new UserAccountDTO();
			userAccountDTO.setId(ua.getId());
			userAccountDTO.setPayId(ua.getPayId());
			userAccountDTO.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.date_sdf));
			userAccountDTO.setAccountSn(ua.getAccountSn());
			userAccountDTO.setShotTime(DateUtil.getCurrentTimeString(Long.valueOf(ua.getAddTime()), DateUtil.short_time_sdf));
			userAccountDTO.setStatus("");
			userAccountDTO.setProcessType(String.valueOf(ua.getProcessType()));
			userAccountDTO.setProcessTypeChar(AccountEnum.getShortStr(ua.getProcessType()));
			userAccountDTO.setProcessTypeName(AccountEnum.getName(ua.getProcessType()));
			userAccountDTO.setNote("");
			String changeAmount = ua.getAmount().compareTo(BigDecimal.ZERO) == 1 ? "+" + ua.getAmount() + "元" : String.valueOf(ua.getAmount() + "元");
			userAccountDTO.setChangeAmount(changeAmount);
			userAccountListDTO.add(userAccountDTO);
		}

		PageInfo<UserAccountDTO> result = new PageInfo<UserAccountDTO>();
		try {
			BeanUtils.copyProperties(result, pageInfo);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		result.setList(userAccountListDTO);
		return result;
	}

	/**
	 * 根据时间段 和 userId构造 账户明细统计
	 * 
	 * @param timeType
	 * @return
	 */
	public UserAccountByTimeDTO createUserAccountTotal(String timeType) {
		Integer userId = SessionUtil.getUserId();
		UserAccountByTimeDTO userAccountByTimeDTO = new UserAccountByTimeDTO();
		List<UserAccount> buyList = new ArrayList<>();
		List<UserAccount> rechargeList = new ArrayList<>();
		List<UserAccount> withdrawList = new ArrayList<>();
		List<UserAccount> rewardList = new ArrayList<>();
		List<UserAccount> userAccountAllTypeList = null;
		Integer startTime = 0;
		Integer endTime = 0;
		Date todayDate = new Date();

		if (ProjectConstant.ALL_TIME.equals(timeType)) {
			UserAccount userAccount = new UserAccount();
			userAccount.setUserId(userId);
			userAccountAllTypeList = userAccountMapper.queryUserAccountBySelective(userAccount);
		} else if (ProjectConstant.TODAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, 1, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountAllTypeList = userAccountMapper.queryUserAccountByTime(userId, null, startTime, endTime);
		} else if (ProjectConstant.BEFORE_6_DAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -5, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountAllTypeList = userAccountMapper.queryUserAccountByTime(userId, null, startTime, endTime);
		} else if (ProjectConstant.BEFORE_29_DAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -28, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountAllTypeList = userAccountMapper.queryUserAccountByTime(userId, null, startTime, endTime);
		} else if (ProjectConstant.BEFORE_89_DAY.equals(timeType)) {
			startTime = DateUtil.getTimeAfterDays(todayDate, -88, 0, 0, 0);
			endTime = DateUtil.getTimeAfterDays(todayDate, 1, 23, 59, 59);
			userAccountAllTypeList = userAccountMapper.queryUserAccountByTime(userId, null, startTime, endTime);
		}

		if (userAccountAllTypeList.size() == 0) {
			return userAccountByTimeDTO;
		}

		for (UserAccount allTypeUa : userAccountAllTypeList) {
			if (ProjectConstant.BUY.equals(allTypeUa.getProcessType())) {
				buyList.add(allTypeUa);
			} else if (ProjectConstant.RECHARGE.equals(allTypeUa.getProcessType())) {
				rechargeList.add(allTypeUa);
			} else if (ProjectConstant.WITHDRAW.equals(allTypeUa.getProcessType())) {
				withdrawList.add(allTypeUa);
			} else if (ProjectConstant.REWARD.equals(allTypeUa.getProcessType())) {
				rewardList.add(allTypeUa);
			}
		}
		// 抵消出票失败的退款 和 提现失败的退款
		BigDecimal backBuyMoney = BigDecimal.ZERO;
		BigDecimal backWithDrawMoney = BigDecimal.ZERO;
		List<String> backBuyOrderSns = buyList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		List<String> backWithDrawOrderSns = withdrawList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		if (backBuyOrderSns.size() > 0) {
			backBuyMoney = userAccountMapper.countBackMoneyByProcessTyepByOrderSns(backBuyOrderSns, userId);
			if (null == backBuyMoney) {
				backBuyMoney = BigDecimal.ZERO;
			}
		}
		if (backWithDrawOrderSns.size() > 0) {
			backWithDrawMoney = userAccountMapper.countBackMoneyByProcessTyepByOrderSns(backWithDrawOrderSns, userId);
			if (null == backWithDrawMoney) {
				backWithDrawMoney = BigDecimal.ZERO;
			}
		}
		DecimalFormat df = new DecimalFormat("0.00");// 保留两位小数
		Double buyMoney = buyList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double totalBuyMoney = buyMoney + backBuyMoney.doubleValue();
		Double rechargeMoney = rechargeList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double withdrawMoney = withdrawList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double totalWithDrawMoney = withdrawMoney + backWithDrawMoney.doubleValue();
		Double rewardMoney = rewardList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		userAccountByTimeDTO.setBuyMoney(String.valueOf(df.format(0 - totalBuyMoney)));
		userAccountByTimeDTO.setRechargeMoney(String.valueOf(df.format(rechargeMoney)));
		userAccountByTimeDTO.setWithDrawMoney(String.valueOf(df.format(0 - totalWithDrawMoney)));
		userAccountByTimeDTO.setRewardMoney(String.valueOf(df.format(rewardMoney)));
		return userAccountByTimeDTO;
	}

	/**
	 * 充值并记录账户流水
	 * 
	 * @param rechargeMoney
	 * @return
	 */
	@Transactional
	public BaseResult<String> rechargeUserMoneyLimit(RecharegeParam recharegeParam) {
		Condition condition = new Condition(UserAccount.class);
		Criteria cri = condition.createCriteria();
		cri.andCondition("user_id =", recharegeParam.getUserId());
		cri.andCondition("pay_id =", recharegeParam.getPayId());
		cri.andCondition("process_type =", ProjectConstant.RECHARGE);
		List<UserAccount> userAccountList = this.findByCondition(condition);
		if (!CollectionUtils.isEmpty(userAccountList)) {
			return ResultGenerator.genResult(MemberEnums.DATA_ALREADY_EXIT_IN_DB.getcode(), MemberEnums.DATA_ALREADY_EXIT_IN_DB.getMsg());
		}

		BigDecimal user_money = BigDecimal.ZERO; // 用户账户变动后的可提现余额
		BigDecimal user_money_limit = BigDecimal.ZERO;// 用户账户变动后的不可提现余额
		BigDecimal curBalance = BigDecimal.ZERO;// 当前变动后的总余额

		Integer userId = recharegeParam.getUserId();
		User user = userService.findById(userId);
		if (null == user) {
			throw new ServiceException(MemberEnums.DBDATA_IS_NULL.getcode(), "用户不存在");
		}

		BigDecimal frozenMoney = user.getFrozenMoney();// 冻结的资金
		User updateUser = new User();
		updateUser.setUserMoneyLimit(recharegeParam.getAmount());
		updateUser.setUserId(userId);

		int moneyRst = userMapper.updateInDBUserMoneyAndUserMoneyLimit(updateUser);
		if (1 != moneyRst) {
			log.error("充值失败");
			throw new ServiceException(MemberEnums.COMMON_ERROR.getcode(), "充值失败");
		}

		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(recharegeParam.getUserId());
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccount.setAccountSn(accountSn);
		userAccount.setAmount(recharegeParam.getAmount());
		userAccount.setProcessType(ProjectConstant.RECHARGE);
		userAccount.setThirdPartName(recharegeParam.getThirdPartName());
		userAccount.setThirdPartPaid(recharegeParam.getThirdPartPaid());
		userAccount.setPayId(recharegeParam.getPayId());
		User updatedUser = userMapper.queryUserExceptPass(userId);
		userAccount.setNote(recharegeParam.getThirdPartName() + "充值" + recharegeParam.getAmount() + "元");
		userAccount.setAddTime(DateUtil.getCurrentTimeLong());
		userAccount.setLastTime(DateUtil.getCurrentTimeLong());
		userAccount.setCurBalance(updatedUser.getUserMoney().add(updatedUser.getUserMoneyLimit()));
		userAccount.setStatus(Integer.valueOf(ProjectConstant.FINISH));

		int rst = userAccountMapper.insertUserAccountBySelective(userAccount);
		if (rst != 1) {
			log.error("生成充值流水失败");
			throw new ServiceException(MemberEnums.COMMON_ERROR.getcode(), "生成充值流水失败");
		}

		return ResultGenerator.genSuccessResult("充值成功", accountSn);
	}

	/**
	 * 预提现并记录账户流水
	 * 
	 * @param rechargeMoney
	 * @return
	 */
	public BaseResult<String> withdrawUserMoney(WithDrawParam withDrawParam) {
		Integer userId = withDrawParam.getUserId();
		User user = userService.findById(userId);
		if (null == user) {
			throw new ServiceException(MemberEnums.DBDATA_IS_NULL.getcode(), "用户不存在");
		}

		if (user.getUserMoney().compareTo(withDrawParam.getAmount()) < 0) {
			return ResultGenerator.genResult(MemberEnums.MONEY_IS_NOT_ENOUGH.getcode(), MemberEnums.MONEY_IS_NOT_ENOUGH.getMsg());
		}

		User updateUser = new User();
		updateUser.setUserMoney(withDrawParam.getAmount());
		updateUser.setUserId(userId);

		int moneyRst = userMapper.reduceUserMoneyInDB(updateUser);
		if (1 != moneyRst) {
			log.error("提现失败");
			return ResultGenerator.genFailResult("提现失败");
		}

		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(withDrawParam.getUserId());
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccount.setAmount(BigDecimal.ZERO.subtract(withDrawParam.getAmount()));
		userAccount.setAccountSn(accountSn);
		userAccount.setProcessType(ProjectConstant.WITHDRAW);
		userAccount.setThirdPartName(withDrawParam.getThirdPartName());
		userAccount.setThirdPartPaid(withDrawParam.getThirdPartPaid());
		userAccount.setPayId(withDrawParam.getPayId());
		userAccount.setAddTime(DateUtil.getCurrentTimeLong());
		userAccount.setLastTime(DateUtil.getCurrentTimeLong());
		User userUpdated = userMapper.queryUserExceptPass(userId);
		userAccount.setCurBalance(userUpdated.getUserMoney().add(userUpdated.getUserMoneyLimit()));
		userAccount.setStatus(1);
		userAccount.setNote(withDrawParam.getThirdPartName() + "提现" + String.format("%.2f", withDrawParam.getThirdPartPaid().doubleValue()) + "元");
		int rst = userAccountMapper.insertUserAccountBySelective(userAccount);
		if (rst != 1) {
			log.error("生成提现流水账户失败");
			return ResultGenerator.genFailResult("生成提现流水账户失败");
		}

		return ResultGenerator.genSuccessResult("提现成功", accountSn);
	}

	/**
	 * 中奖后批量更新用户账户的可提现余额,dealType = 1,自动；dealType = 2,手动
	 * 
	 * @param userIdAndRewardList
	 */
	public BaseResult<String> batchUpdateUserAccount(List<UserIdAndRewardDTO> dtos, Integer dealType) {
		List<UserIdAndRewardDTO> oldUserIdAndRewardDtos = new ArrayList<UserIdAndRewardDTO>(dtos);
		List<UserIdAndRewardDTO> userIdAndRewardList = new ArrayList<UserIdAndRewardDTO>(dtos);
		BigDecimal limitValue = BigDecimal.ZERO;
		if (1 == dealType) {
			limitValue = this.queryBusinessLimit(CommonConstants.BUSINESS_ID_REWARD);
			if (limitValue.compareTo(BigDecimal.ZERO) <= 0) {
				log.error("请前往后台管理系统设置派奖金额阈值,不予派奖");
				return ResultGenerator.genFailResult("请前往后台管理系统设置派奖金额阈值");
			}

			Double limitValueDouble = limitValue.doubleValue();
			this.updateBigRewardChecking(userIdAndRewardList, limitValueDouble);
			userIdAndRewardList.removeIf(s -> s.getReward().doubleValue() >= limitValueDouble);
		}
		if (userIdAndRewardList.size() == 0) {
			return ResultGenerator.genSuccessResult("没有要自动开奖的订单");
		}

		log.info("=^_^= =^_^= =^_^= =^_^= 派奖开始,派奖数据包括:" + JSON.toJSONString(userIdAndRewardList));

		// 查询是否已经派发奖金,并过滤掉
		List<String> orderSnList = userIdAndRewardList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		List<String> rewardOrderSnList = userAccountMapper.queryUserAccountRewardByOrdersn(orderSnList);
		if (rewardOrderSnList.size() > 0) {
			log.error("含有已派发过奖金的订单号，已被过滤,订单号包括：" + Joiner.on(",").join(rewardOrderSnList));
			OrderSnListParam orderSnAlreadyRewardListParam = new OrderSnListParam();
			orderSnAlreadyRewardListParam.setOrderSnlist(rewardOrderSnList);
			BaseResult<Integer> orderRst = orderService.updateOrderStatusRewarded(orderSnAlreadyRewardListParam);
			userIdAndRewardList.removeIf(s -> rewardOrderSnList.contains(s.getOrderSn()));
		}

		Integer accountTime = DateUtil.getCurrentTimeLong();
		for (UserIdAndRewardDTO uDTO : userIdAndRewardList) {
			User updateUserMoney = new User();
			updateUserMoney.setUserId(uDTO.getUserId());
			updateUserMoney.setUserMoney(uDTO.getReward());
			userMapper.updateInDBUserMoneyAndUserMoneyLimit(updateUserMoney);
			UserAccount userAccountParam = new UserAccount();
			String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
			userAccountParam.setAccountSn(accountSn);
			userAccountParam.setOrderSn(uDTO.getOrderSn());
			userAccountParam.setUserId(uDTO.getUserId());
			userAccountParam.setAmount(uDTO.getReward());
			Integer userId = uDTO.getUserId();
			User user = userMapper.queryUserExceptPass(userId);
			userAccountParam.setCurBalance(user.getUserMoney().add(user.getUserMoneyLimit()));
			userAccountParam.setProcessType(ProjectConstant.REWARD);
			userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
			userAccountParam.setAddTime(accountTime);
			userAccountParam.setStatus(Integer.valueOf(ProjectConstant.FINISH));
			int insertRst = userAccountMapper.insertUserAccountBySelective(userAccountParam);
			if (1 != insertRst) {
				log.error("中奖订单号为" + uDTO.getOrderSn() + "生成中奖流水失败");
			} else {
				log.error("用户" + uDTO.getUserId() + "中奖订单号为" + uDTO.getOrderSn() + "奖金派发完成");
			}
		}

		log.info("更新用户中奖订单为已派奖开始");
		List<String> orderSnRewaredList = userIdAndRewardList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		OrderSnListParam orderSnListParam = new OrderSnListParam();
		orderSnListParam.setOrderSnlist(orderSnRewaredList);
		BaseResult<Integer> orderRst = orderService.updateOrderStatusRewarded(orderSnListParam);
		if (0 != orderRst.getCode()) {
			log.error("更新用户订单为已派奖失败");
		}
		log.info("更新用户中奖订单为已派奖成功");

		// 推送消息
		saveRewardMessageAsync(userIdAndRewardList, accountTime);

		// 记录中奖信息
		this.updateLotteryWinning(oldUserIdAndRewardDtos);
		log.info("=^_^= =^_^= =^_^= =^_^= " + DateUtil.getCurrentDateTime() + "用户派发奖金完成" + "=^_^= =^_^= =^_^= =^_^= ");

		return ResultGenerator.genSuccessResult("用户派发奖金完成");
	}

	/**
	 * 把大额奖金设置为派奖审核中
	 */
	public void updateBigRewardChecking(List<UserIdAndRewardDTO> userIdAndRewardList, Double limitValueDouble) {
		List<UserIdAndRewardDTO> userIdAndRewardDTOList = userIdAndRewardList.stream().filter(s -> s.getReward().doubleValue() >= limitValueDouble).collect(Collectors.toList());
		if (userIdAndRewardDTOList.size() == 0) {
			return;
		}

		List<String> orderSnRewaredList = userIdAndRewardList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		UpdateOrderStatusByAnotherStatusParam param = new UpdateOrderStatusByAnotherStatusParam();
		param.setOrderSnlist(orderSnRewaredList);
		param.setOrderStatusAfter("7");// 派奖审核中
		param.setOrderStatusBefore("6");// 派奖中
		BaseResult<Integer> orderRst = orderService.updateOrderStatusAnother(param);
		if (orderRst.getCode() != 0) {
			log.error("大额奖金订单设置为派奖审核中异常");
		}
	}

	/**
	 * 针对单场竞猜答题用户中奖后批量更新到不可提现余额
	 * 
	 * @param userIdAndRewardList
	 */
	@Transactional
	public BaseResult<String> batchUpdateUserRewardToUserMoneyLimit(List<UserIdAndRewardDTO> dtos) {
		List<UserIdAndRewardDTO> userIdAndRewardList = new ArrayList<UserIdAndRewardDTO>(dtos);
		log.info("======================================派奖开始,派奖数据包括:" + JSON.toJSONString(userIdAndRewardList));
		// 查询是否已经派发奖金,并过滤掉
		List<String> orderSnList = userIdAndRewardList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		List<String> rewardOrderSnList = userAccountMapper.queryUserAccountRewardByOrdersn(orderSnList);
		if (rewardOrderSnList.size() > 0) {
			log.error("含有已派发过奖金的单号，已被过滤,单号包括：" + Joiner.on(",").join(rewardOrderSnList));
			userIdAndRewardList.removeIf(s -> rewardOrderSnList.contains(s.getOrderSn()));
		}

		Integer accountTime = DateUtil.getCurrentTimeLong();
		for (UserIdAndRewardDTO uDTO : userIdAndRewardList) {
			User updateUserMoney = new User();
			updateUserMoney.setUserId(uDTO.getUserId());
			// 奖金派发到不可提现余额里
			updateUserMoney.setUserMoneyLimit(uDTO.getReward());
			userMapper.updateInDBUserMoneyAndUserMoneyLimit(updateUserMoney);
			UserAccount userAccountParam = new UserAccount();
			String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
			userAccountParam.setAccountSn(accountSn);
			userAccountParam.setOrderSn(uDTO.getOrderSn());
			userAccountParam.setUserId(uDTO.getUserId());
			userAccountParam.setAmount(uDTO.getReward());
			userAccountParam.setProcessType(ProjectConstant.REWARD);
			userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
			userAccountParam.setAddTime(accountTime);
			userAccountParam.setNote(uDTO.getNote());
			userAccountParam.setStatus(Integer.valueOf(ProjectConstant.FINISH));
			int insertRst = userAccountMapper.insertUserAccountBySelective(userAccountParam);
			if (1 != insertRst) {
				log.error("中奖订号为" + uDTO.getOrderSn() + "生成中奖流水失败");
			} else {
				log.error("用户" + uDTO.getUserId() + "中奖单号为" + uDTO.getOrderSn() + "奖金派发完成");
			}
		}
		// 推送消息
		// saveRewardMessageAsync(userIdAndRewardList, accountTime);
		// 记录中奖信息
		this.updateLotteryWinning(dtos);
		log.info("===========================================" + DateUtil.getCurrentDateTime() + "用户奖金派发完成" + "===========================================");
		return ResultGenerator.genSuccessResult("用户派发奖金完成");
	}

	/**
	 * 异步保存中奖消息
	 * 
	 * @param list
	 */
	@Async
	public void saveRewardMessageAsync(List<UserIdAndRewardDTO> list, Integer accountTime) {
		List<Integer> userIdList = list.stream().map(s -> s.getUserId()).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(userIdList)) {
			return;
		}
		log.info(" 00000   " + JSONHelper.bean2json(userIdList));
		List<User> userList = userMapper.queryUserByUserIds(userIdList);
		if (CollectionUtils.isEmpty(userList)) {
			return;
		}

		for (UserIdAndRewardDTO u : list) {
			String gameDesc = u.getLotteryName();
			if(StringUtils.isEmpty(gameDesc)){
				log.error("订单orderSn={},发送消息lotteryId ={},lotteryName is null",u.getOrderSn(),u.getLotteryClassifyId());
				gameDesc = "竞彩足球";
			}
			DlMessage messageAddParam = new DlMessage();
			messageAddParam.setTitle(CommonConstants.FORMAT_REWARD_TITLE);
			messageAddParam.setContent(MessageFormat.format(CommonConstants.FORMAT_REWARD_CONTENT, u.getReward()));
			messageAddParam.setContentDesc(CommonConstants.FORMAT_REWARD_CONTENT_DESC);
			// messageAddParam.setContentUrl("www.baidu.com");//通知暂时不需要
			messageAddParam.setSender(u.getUserId());
			messageAddParam.setMsgType(0);
			messageAddParam.setReceiver(u.getUserId());
			String clientId = null;
			for (User user : userList) {
				if (user.getUserId().equals(u.getUserId())) {
					messageAddParam.setReceiverMobile(user.getMobile());
					clientId = user.getPushKey();
				}
				continue;
			}
			messageAddParam.setObjectType(1);
			messageAddParam.setMsgUrl("");// 通知暂时不需要
			messageAddParam.setSendTime(accountTime);
			messageAddParam.setMsgDesc(MessageFormat.format(CommonConstants.FORMAT_REWARD_MSG_DESC, gameDesc,u.getBetMoney(), u.getBetTime()));
			userMessageService.save(messageAddParam);
			// push
			if (StringUtils.isNotBlank(clientId)) {
				String content = MessageFormat.format(CommonConstants.FORMAT_REWARD_PUSH_DESC,gameDesc, u.getReward());
				GeTuiMessage getuiMessage = new GeTuiMessage(CommonConstants.FORMAT_REWARD_PUSH_TITLE, content, DateUtil.getCurrentTimeLong());
				geTuiUtil.pushMessage(clientId, getuiMessage);
			}
		}
	}

	/**
	 * 查询业务值得限制：CommonConstants 中9-派奖限制 8-提现限制
	 * 
	 * @return
	 */
	public BigDecimal queryBusinessLimit(Integer businessId) {
		// 检查是否设置了派奖阈值
		SysConfigDTO sysDTO = sysConfigService.querySysConfig(businessId);
		if (sysDTO == null) {
			log.warn("派奖前，请前往后台管理设置派奖的奖金阈值");
			return BigDecimal.ZERO;
		}
		BigDecimal limitValue = sysDTO.getValue();
		return limitValue;
	}

	/**
	 * 回滚含有余额部分的付款所用的钱
	 * 
	 * @param String
	 *            orderSn,String surplus
	 */
	@Transactional
	public BaseResult<SurplusPaymentCallbackDTO> rollbackUserAccountChangeByPay(SurplusPayParam surplusPayParam) {
		String inPrams = JSON.toJSONString(surplusPayParam);
		log.info(DateUtil.getCurrentDateTime() + "使用到了部分或全部余额时候回滚支付传递的参数:" + inPrams);

		OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(surplusPayParam.getOrderSn());
		BaseResult<OrderDTO> orderDTORst = orderService.getOrderInfoByOrderSn(orderSnParam);
		if (orderDTORst.getCode() != 0) {
			return ResultGenerator.genFailResult(orderDTORst.getMsg());
		}
		OrderDTO orderDTO = orderDTORst.getData();
		if (null == orderDTO) {
			return ResultGenerator.genFailResult("没有该笔订单" + surplusPayParam.getOrderSn() + "，无法回滚账户");
		}

		Integer userId = orderDTO.getUserId();
		if (null == userId) {
			return ResultGenerator.genFailResult("该笔订单" + surplusPayParam.getOrderSn() + "userId为空，无法回滚账户");
		}

		User user = userMapper.selectUserFoUpdateByUserId(userId);//锁用户信息;
		if (null == user) {
			return ResultGenerator.genFailResult("没有这个用户的用户id，无法回滚账户");
		}

		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userId);
		userAccount.setOrderSn(surplusPayParam.getOrderSn());
		userAccount.setProcessType(ProjectConstant.BUY);
		List<UserAccount> userAccountList = userAccountMapper.queryUserAccountBySelective(userAccount);
		if (CollectionUtils.isEmpty(userAccountList)) {
			return ResultGenerator.genFailResult("订单号为" + surplusPayParam.getOrderSn() + "没有账户记录，无法回滚");
		}

		UserAccount userAccountRoll = new UserAccount();
		userAccountRoll.setUserId(userId);
		userAccountRoll.setThirdPartPaid(surplusPayParam.getThirdPartPaid());
		userAccountRoll.setOrderSn(surplusPayParam.getOrderSn());
		userAccountRoll.setProcessType(ProjectConstant.ACCOUNT_ROLLBACK);
		List<UserAccount> userAccountListRoll = userAccountMapper.queryUserAccountBySelective(userAccountRoll);
		if (!CollectionUtils.isEmpty(userAccountListRoll)) {
			return ResultGenerator.genFailResult("订单号为" + surplusPayParam.getOrderSn() + "已经回滚，无法再次回滚");
		}

		User updateUser = new User();
		BigDecimal userSurplus = orderDTO.getUserSurplus();
		BigDecimal userSurplusLimit = orderDTO.getUserSurplusLimit();
		boolean isModify = false;
		if (userSurplus != null && userSurplus.doubleValue() > 0) {
			log.info("user money: " + user.getUserMoney());
			BigDecimal user_money = user.getUserMoney().add(userSurplus);
			updateUser.setUserMoney(user_money);
			isModify = true;
		}

		if (userSurplusLimit != null && userSurplusLimit.doubleValue() > 0) {
			BigDecimal user_money_limit = user.getUserMoneyLimit().add(userSurplusLimit);
			updateUser.setUserMoneyLimit(user_money_limit);
			isModify = true;
		}

		if (isModify) {
			updateUser.setUserId(user.getUserId());
			int moneyRst = userMapper.updateUserMoneyAndUserMoneyLimit(updateUser);
		}

		UserAccount userAccountParam = new UserAccount();
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccountParam.setAccountSn(accountSn);
		userAccountParam.setUserId(userId);
		userAccountParam.setAmount(orderDTO.getUserSurplus().add(orderDTO.getUserSurplusLimit()));
		User curUser = userService.findById(userId);
		BigDecimal curBalance = curUser.getUserMoney().add(curUser.getUserMoneyLimit());
		userAccountParam.setCurBalance(curBalance);
		userAccountParam.setProcessType(ProjectConstant.ACCOUNT_ROLLBACK);
		userAccountParam.setOrderSn(orderDTO.getOrderSn());
		userAccountParam.setPaymentName(orderDTO.getPayName());
		userAccountParam.setThirdPartName(StringUtils.isEmpty(orderDTO.getPayName()) ? "" : orderDTO.getPayName());
		userAccountParam.setAddTime(DateUtil.getCurrentTimeLong());
		userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
		userAccountParam.setPayId(String.valueOf(orderDTO.getPayId()));
		int insertRst = userAccountMapper.insertUserAccountBySelective(userAccountParam);

		SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = new SurplusPaymentCallbackDTO();
		surplusPaymentCallbackDTO.setSurplus(BigDecimal.ZERO);
		surplusPaymentCallbackDTO.setUserSurplus(BigDecimal.ZERO);
		surplusPaymentCallbackDTO.setUserSurplusLimit(BigDecimal.ZERO);
		surplusPaymentCallbackDTO.setCurBalance(curBalance);

		return ResultGenerator.genSuccessResult("success", surplusPaymentCallbackDTO);
	}

	/***
	 * 出票失败，资金回滚到可提现金额中
	 * 
	 * @param memRollParam
	 * @return
	 */
	public BaseResult<Object> rollbackUserMoneyOrderFailure(MemRollParam memRollParam) {
		String orderSn = memRollParam.getOrderSn();
		Integer userId = memRollParam.getUserId();
		BigDecimal amt = memRollParam.getAmt();
		log.info("[rollbackUserMoneyOrderFailure]" + " orderSn:" + orderSn + " userId:" + userId + " amt:" + amt);
		// 查看该order是否存在
		OrderSnParam orderSnParam = new OrderSnParam();
		orderSnParam.setOrderSn(orderSn);
		BaseResult<OrderDTO> orderDTORst = orderService.getOrderInfoByOrderSn(orderSnParam);
		if (orderDTORst.getCode() != 0 || orderDTORst.getData() == null || StringUtils.isEmpty(orderDTORst.getData().getOrderSn())) {
			log.info("[rollbackUserMoneyOrderFailure]" + "该订单不存在 orderSn:" + orderSn);
			return ResultGenerator.genFailResult("该订单不存在");
		}
		// 账户流水查看
		UserAccount userAccountRoll = new UserAccount();
		userAccountRoll.setUserId(userId);
		userAccountRoll.setThirdPartPaid(amt);
		userAccountRoll.setOrderSn(orderSn);
		userAccountRoll.setProcessType(ProjectConstant.ACCOUNT_ROLLBACK);
		List<UserAccount> userAccountListRoll = userAccountMapper.queryUserAccountBySelective(userAccountRoll);
		if (!CollectionUtils.isEmpty(userAccountListRoll)) {
			log.info("[rollbackUserMoneyOrderFailure]" + " 订单已经回滚，无法再次回滚");
			return ResultGenerator.genFailResult("订单号为" + orderSn + "已经回滚，无法再次回滚");
		}
		User user = userService.findById(userId);
		if (user == null) {
			log.info("[rollbackUserMoneyOrderFailure]" + " 未查询到该用户 userId:" + userId);
			return ResultGenerator.genFailResult("[rollbackUserMoneyOrderFailure]" + " 未查询到该用户 userId:" + userId);
		}
		user = new User();
		// user.setUserMoney(amt);
		// 调整为不可提现余额
		user.setUserMoneyLimit(amt);
		user.setUserId(userId);
		int cnt = userMapper.updateInDBUserMoneyAndUserMoneyLimit(user);
		log.info("[rollbackUserMoneyOrderFailure]" + " userId:" + userId + " amt:" + amt + " result cnt:" + cnt);

		// ===========记录退款流水====================
		UserAccount userAccountParamByType = new UserAccount();
		Integer accountType = ProjectConstant.ACCOUNT_ROLLBACK;
		log.info("===========更新用户流水表=======:" + accountType);
		userAccountParamByType.setProcessType(accountType);
		userAccountParamByType.setAmount(amt);
		userAccountParamByType.setBonusPrice(BigDecimal.ZERO);// 暂无红包金额
		userAccountParamByType.setOrderSn(orderSn);
		userAccountParamByType.setThirdPartPaid(BigDecimal.ZERO);
		userAccountParamByType.setUserId(userId);
		userAccountParamByType.setAddTime(DateUtil.getCurrentTimeLong());
		userAccountParamByType.setLastTime(DateUtil.getCurrentTimeLong());
		userAccountParamByType.setParentSn("");
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccountParamByType.setAccountSn(accountSn);
		PayLogDetailDTO payLog = null;
		PayLogOrderSnParam paySnParam = new PayLogOrderSnParam();
		paySnParam.setOrderSn(orderSn);
		BaseResult<PayLogDetailDTO> bR = payMentService.queryPayLogByOrderSn(paySnParam);
		if (bR.getCode() == 0 && bR.getData() != null) {
			payLog = bR.getData();
		}
		if (payLog != null) {
			log.info("[rollbackUserMoneyOrderFailure]" + " 已查询到paylog信息...");
			String payName = "";
			String payCode = payLog.getPayCode();
			userAccountParamByType.setPayId(payLog.getLogId() + "");
			if (payCode.equals("app_weixin") || payCode.equals("app_weixin_h5")) {
				payName = "微信";
			} else {
				payName = "银行卡";
			}
			userAccountParamByType.setPaymentName(payName);
			userAccountParamByType.setThirdPartName(payName);
		} else {
			userAccountParamByType.setPayId("0");
			userAccountParamByType.setPaymentName("");
			userAccountParamByType.setThirdPartName("");
		}
		int count = insertUserAccount(userAccountParamByType);
		log.info("退款成功记录流水成功 cnt:" + count);
		return ResultGenerator.genSuccessResult();
	}

	/**
	 * 提现失败和审核拒绝回滚账户可提现余额
	 * 
	 * @param String
	 *            orderSn,String surplus
	 */
	@Transactional
	public BaseResult<SurplusPaymentCallbackDTO> rollbackUserMoneyWithDrawFailure(MemWithDrawSnParam memWithDrawSnParam) {
		String inPrams = JSON.toJSONString(memWithDrawSnParam);
		log.info(DateUtil.getCurrentDateTime() + "提现失败回滚账户可提现余额的参数:" + inPrams);
		Integer userId = SessionUtil.getUserId();
		if (null == userId) {
			userId = memWithDrawSnParam.getUserId();
		}

		Condition condition = new Condition(UserAccount.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id =", userId);
		criteria.andCondition("pay_id =", memWithDrawSnParam.getWithDrawSn());
		criteria.andCondition("process_type =", ProjectConstant.ACCOUNT_ROLLBACK);
		List<UserAccount> withdrawAccountList = this.findByCondition(condition);
		if (withdrawAccountList.size() > 0) {
			return ResultGenerator.genFailResult("提现单号" + memWithDrawSnParam.getWithDrawSn() + "已经进行了回滚，无法再次回滚");
		}
		WithDrawSnAndUserIdParam paywithDrawSnParam = new WithDrawSnAndUserIdParam();
		paywithDrawSnParam.setWithDrawSn(memWithDrawSnParam.getWithDrawSn());
		paywithDrawSnParam.setUserId(userId);
		BaseResult<com.dl.shop.payment.dto.UserWithdrawDTO> withDrawRst = payMentService.queryUserWithdrawBySnAndUserId(paywithDrawSnParam);
		log.info("回滚时，查询提现单参数：" + JSON.toJSONString(withDrawRst));
		if (withDrawRst.getCode() != 0) {
			log.info("[rollbackUserMoneyWithDrawFailure]" + " 查询提现单失败...");
			return ResultGenerator.genFailResult("查询提现单失败，无法对这笔提现单对应的提现预扣款进行回滚");
		}
		com.dl.shop.payment.dto.UserWithdrawDTO userWithdrawDTO = withDrawRst.getData();
		if (null == userWithdrawDTO) {
			log.info("[rollbackUserMoneyWithDrawFailure]" + " 不存在，无法对这笔提现单对应的提现预扣款进行回滚...");
			return ResultGenerator.genFailResult("提现单" + paywithDrawSnParam.getWithDrawSn() + "不存在，无法对这笔提现单对应的提现预扣款进行回滚");
		}
		log.info("[rollback]" + " status:" + userWithdrawDTO.getStatus());
		if (!userWithdrawDTO.getStatus().equals(ProjectConstant.FAILURE)) {
			return ResultGenerator.genFailResult("提现单" + paywithDrawSnParam.getWithDrawSn() + "未提现失败，无法对这笔提现单对应的提现预扣款进行回滚");
		}

		User user = userService.findById(userId);
		User updateUser = new User();
		updateUser.setUserId(userId);
		updateUser.setUserMoney(userWithdrawDTO.getAmount());
		userMapper.updateInDBUserMoneyAndUserMoneyLimit(updateUser);
		User userUpdated = userMapper.queryUserExceptPass(userId);
		log.info("回滚后" + userId + "账户值：" + userUpdated.getUserMoney().add(userUpdated.getUserMoneyLimit()));

		log.info("开始生成回滚账户流水");
		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userId);
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccount.setAmount(userWithdrawDTO.getAmount());
		userAccount.setAccountSn(accountSn);
		userAccount.setProcessType(ProjectConstant.ACCOUNT_ROLLBACK);
		userAccount.setThirdPartName("");
		userAccount.setThirdPartPaid(BigDecimal.ZERO);
		userAccount.setPayId(userWithdrawDTO.getWithdrawalSn());
		userAccount.setAddTime(DateUtil.getCurrentTimeLong());
		userAccount.setLastTime(DateUtil.getCurrentTimeLong());
		userAccount.setCurBalance(userUpdated.getUserMoney().add(userUpdated.getUserMoneyLimit()));
		userAccount.setStatus(1);
		userAccount.setNote("提现回滚" + userWithdrawDTO.getAmount());
		int rst = userAccountMapper.insertUserAccountBySelective(userAccount);
		log.info("生成回滚账户流水返回值" + rst);
		if (rst != 1) {
			log.error("生成提现流水账户失败");
			return ResultGenerator.genFailResult("生成提现流水账户失败");
		}
		log.info("生成回滚账户流水结束");
		SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = new SurplusPaymentCallbackDTO();
		surplusPaymentCallbackDTO.setCurBalance(user.getUserMoney().add(userWithdrawDTO.getAmount()));
		return ResultGenerator.genSuccessResult("success", surplusPaymentCallbackDTO);
	}

	/**
	 * 统计当月的各个用途的资金和
	 * 
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

		for (UserAccount u : userAccountList) {
			if (ProjectConstant.BUY.equals(u.getProcessType())) {
				buyList.add(u);
			} else if (ProjectConstant.RECHARGE.equals(u.getProcessType())) {
				rechargeList.add(u);
			} else if (ProjectConstant.WITHDRAW.equals(u.getProcessType())) {
				withdrawList.add(u);
			} else if (ProjectConstant.REWARD.equals(u.getProcessType())) {
				rewardList.add(u);
			}
		}

		// 抵消出票失败的退款 和 提现失败的退款
		BigDecimal backBuyMoney = BigDecimal.ZERO;
		BigDecimal backWithDrawMoney = BigDecimal.ZERO;
		List<String> backBuyOrderSns = buyList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		List<String> backWithDrawOrderSns = withdrawList.stream().map(s -> s.getOrderSn()).collect(Collectors.toList());
		if (backBuyOrderSns.size() > 0) {
			backBuyMoney = userAccountMapper.countBackMoneyByProcessTyepByOrderSns(backBuyOrderSns, userId);
			if (null == backBuyMoney) {
				backBuyMoney = BigDecimal.ZERO;
			}
		}
		if (backWithDrawOrderSns.size() > 0) {
			backWithDrawMoney = userAccountMapper.countBackMoneyByProcessTyepByOrderSns(backWithDrawOrderSns, userId);
			if (null == backWithDrawMoney) {
				backWithDrawMoney = BigDecimal.ZERO;
			}
		}

		DecimalFormat df = new DecimalFormat("0.00");// 保留两位小数
		Double buyMoney = buyList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double totalBuyMoney = buyMoney + backBuyMoney.doubleValue();
		Double rechargeMoney = rechargeList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double withdrawMoney = withdrawList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
		Double totalWithDrawMoney = withdrawMoney + backWithDrawMoney.doubleValue();
		Double rewardMoney = rewardList.stream().map(s -> s.getAmount().doubleValue()).reduce(Double::sum).orElse(0.00);

		userAccountCurMonthDTO.setBuyMoney(String.valueOf(df.format(0 - totalBuyMoney)));
		userAccountCurMonthDTO.setRechargeMoney(String.valueOf(df.format(rechargeMoney)));
		userAccountCurMonthDTO.setWithDrawMoney(String.valueOf(df.format(0 - totalWithDrawMoney)));
		userAccountCurMonthDTO.setRewardMoney(String.valueOf(df.format(rewardMoney)));

		return ResultGenerator.genSuccessResult("统计当月的各个用途的资金和成功", userAccountCurMonthDTO);
	}

	/**
	 * 保存账户流水
	 * 
	 * @param amount
	 * @param orderSn
	 * @param paymentCode
	 * @param paymentName
	 * @param processType
	 * @return
	 */
	@Transactional
	public String saveAccount(UserAccountParam userAccountParam) {
		Integer userId = SessionUtil.getUserId();

		User user = userService.findById(userId);
		if (null == user) {
			return "";
		}
		BigDecimal curBalance = user.getUserMoney().add(user.getUserMoneyLimit()).subtract(userAccountParam.getAmount());
		UserAccount userAccount = new UserAccount();
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccount.setAccountSn(accountSn);
		userAccount.setAddTime(DateUtil.getCurrentTimeLong());
		userAccount.setLastTime(userAccountParam.getLastTime());
		userAccount.setAmount(userAccountParam.getAmount());
		userAccount.setCurBalance(curBalance);
		if (StringUtils.isNotEmpty(userAccountParam.getOrderSn())) {
			userAccount.setOrderSn(userAccountParam.getOrderSn());
		}
		userAccount.setPayId(userAccountParam.getPayId() == null ? "" : userAccountParam.getPayId());
		userAccount.setPaymentName(userAccountParam.getPaymentName());
		userAccount.setThirdPartName(StringUtils.isEmpty(userAccountParam.getThirdPartName()) ? "" : userAccountParam.getThirdPartName());
		userAccount.setUserSurplus(userAccountParam.getUserSurplus() == null ? BigDecimal.ZERO : userAccountParam.getUserSurplus());
		userAccount.setUserSurplusLimit(userAccountParam.getUserSurplusLimit() == null ? BigDecimal.ZERO : userAccountParam.getUserSurplusLimit());
		userAccount.setThirdPartPaid(userAccountParam.getThirdPartPaid() == null ? BigDecimal.ZERO : userAccountParam.getThirdPartPaid());
		userAccount.setProcessType(userAccountParam.getAccountType());
		userAccount.setUserId(user.getUserId());
		userAccount.setStatus(userAccountParam.getStatus());
		userAccount.setNote(userAccountParam.getNote());
		userAccount.setParentSn("");
		userAccount.setBonusPrice(userAccountParam.getBonusPrice() != null ? userAccountParam.getBonusPrice() : BigDecimal.ZERO);

		int rst = userAccountMapper.insertUserAccount(userAccount);
		if (rst != 1) {
			log.error("生成流水账户失败");
			return "";
		}

		return accountSn;
	}

	/**
	 * 给第三方支付提供的记录日志的方法
	 * 
	 * @param userAccountParamByType
	 * @return
	 */
	public String saveUserAccountForThirdPay(UserAccountParamByType userAccountParamByType) {
		Condition condition = new Condition(UserAccount.class);
		Criteria cri = condition.createCriteria();
		cri.andCondition("user_id =", userAccountParamByType.getUserId());
		cri.andCondition("pay_id =", userAccountParamByType.getPayId());
		cri.andCondition("process_type =", userAccountParamByType.getAccountType());
		List<UserAccount> userAccountList = this.findByCondition(condition);
		if (!CollectionUtils.isEmpty(userAccountList)) {
			return "";
		}

		UserAccount userAccount = new UserAccount();
		String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
		userAccount.setAccountSn(accountSn);
		userAccount.setProcessType(userAccountParamByType.getAccountType());
		userAccount.setAmount(BigDecimal.ZERO.subtract(userAccountParamByType.getAmount()));
		userAccount.setOrderSn(userAccountParamByType.getOrderSn());
		userAccount.setPayId(String.valueOf(userAccountParamByType.getPayId()));
		userAccount.setPaymentName(userAccountParamByType.getPaymentName());
		userAccount.setUserId(userAccountParamByType.getUserId());
		userAccount.setNote(this.createNoteForThirdPay(userAccountParamByType));
		userAccount.setAddTime(DateUtil.getCurrentTimeLong());
		userAccount.setLastTime(DateUtil.getCurrentTimeLong());
		userAccount.setBonusPrice(userAccountParamByType.getBonusPrice());
		userAccount.setThirdPartName(userAccountParamByType.getThirdPartName());
		userAccount.setThirdPartPaid(userAccountParamByType.getThirdPartPaid());
		userAccount.setStatus(Integer.valueOf(ProjectConstant.FINISH));

		int rst = userAccountMapper.insertUserAccountBySelective(userAccount);
		if (rst != 1) {
			log.error("生成流水账户失败");
			return "";
		}
		return accountSn;
	}

	/**
	 * 查询提现的状态
	 * 
	 * @param processType
	 * @param accountId
	 * @return
	 */
	public String showStatus(UserAccount ua) {
		String withDrawSn = ua.getPayId();
		if (StringUtils.isEmpty(withDrawSn)) {
			return "";
		}
		WithDrawSnParam withDrawSnParam = new WithDrawSnParam();
		withDrawSnParam.setWithDrawSn(withDrawSn);
		BaseResult<UserWithdrawDetailDTO> withDrawDTORst = payMentService.querUserWithDrawDetail(withDrawSnParam);
		if (withDrawDTORst.getCode() != 0) {
			return "";
		}

		String withDrawStatus = withDrawDTORst.getData().getStatus();
		if (withDrawStatus.equals(ProjectConstant.FINISH)) {
			return "状态:提现成功";
		} else if (withDrawStatus.equals(ProjectConstant.NOT_FINISH)) {
			return "状态:提现中";
		} else if (withDrawStatus.equals(ProjectConstant.FAILURE)) {
			return "状态:提现失败";
		}
		return "";
	}

	/**
	 * 根据操作类型返回不同的文字
	 * 
	 * @param processType
	 *            1-奖金 2-充值 3-购彩 4-提现 5-红包 6-账户回滚
	 * @return
	 */
	public String createProcessTypeString(Integer processType) {
		String str = "";
		switch (processType) {
		case 1:
			str = "奖";
			break;
		case 2:
			str = "充";
			break;
		case 3:
			str = "购";
			break;
		case 4:
			str = "提";
			break;
		case 5:
			str = "红";
			break;
		case 6:
			str = "返";
			break;
		}
		return str;
	}

	public List<UserAccount> findByProcessType(Integer processType) {
		Condition c = new Condition(UserAccount.class);
		Criteria criteria = c.createCriteria();
		criteria.andCondition("process_type =", processType);
		return this.findByCondition(c);
	}

	// /**
	// * 高速批量更新User 中的userMoney 10万条数据 18s
	// * @param list
	// */
	// public int updateBatchUserMoney(List<UserIdAndRewardDTO> list) {
	// Connection conn = null;
	// try {
	// Class.forName(dbDriver);
	// conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName,
	// dbPass);
	// conn.setAutoCommit(false);
	// String sql = "UPDATE dl_user SET user_money =  ? WHERE user_id = ?";
	// PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql,
	// ResultSet.TYPE_SCROLL_SENSITIVE,
	// ResultSet.CONCUR_READ_ONLY);
	// for (int x = 0, size = list.size(); x < size; x++) {
	// prest.setBigDecimal(1, list.get(x).getUserMoney());
	// prest.setInt(2, list.get(x).getUserId());
	// prest.addBatch();
	// }
	// prest.executeBatch();
	// conn.commit();
	// conn.close();
	// return 1;
	// }catch (Exception ex) {
	// try {
	// conn.rollback();
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error(DateUtil.getCurrentDateTime() +
	// "执行updateBatchUserMoney异常，且回滚异常:" + ex.getMessage());
	// return -1;
	// }
	// log.error(DateUtil.getCurrentDateTime() +
	// "执行updateBatchUserMoney异常，回滚成功:" + ex.getMessage());
	// return 0;
	// }
	// }
	//
	// /**
	// * 高速批量插入UserAccount 10万条数据 18s
	// * @param list
	// * @return 0：执行批量操作异常，但是回滚成功 -1 执行批量操作异常，且回滚失败 1批量更新成功
	// */
	// public int batchInsertUserAccount(List<UserAccountParam> list) {
	// int rowsTmp = 0;
	// int commitNum = 1000;
	//
	// Connection conn = null;
	// try {
	// Class.forName(dbDriver);
	// conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName,
	// dbPass);
	// conn.setAutoCommit(false);
	// String sql =
	// "INSERT INTO dl_user_account(account_sn,user_id,amount,add_time,process_type,order_sn,note,status) VALUES(?,?,?,?,?,?,?,?)";
	// PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql,
	// ResultSet.TYPE_FORWARD_ONLY,
	// ResultSet.CONCUR_READ_ONLY);
	// Integer addTime = DateUtil.getCurrentTimeLong();
	// for (int x = 0, size = list.size(); x < size; x++) {
	// prest.setString(1, list.get(x).getAccountSn());
	// prest.setInt(2, list.get(x).getUserId());
	// prest.setBigDecimal(3, list.get(x).getAmount());
	// prest.setInt(4, addTime);
	// prest.setInt(5, ProjectConstant.REWARD);
	// prest.setString(6, list.get(x).getOrderSn());
	// prest.setString(7, list.get(x).getNote());
	// prest.setInt(8, list.get(x).getStatus());
	// prest.addBatch();
	//
	// if(rowsTmp%commitNum == 0){//每1000条记录一提交
	// prest.executeBatch();
	// conn.commit();
	// if (null==conn) { //如果连接关闭了 就在创建一个 为什么要这样 原因是 conn.commit()后可能conn被关闭
	// conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName,
	// dbPass);
	// conn.setAutoCommit(false);
	// }
	// }
	// rowsTmp++;
	// }
	//
	// prest.executeBatch();
	// conn.commit();
	//
	// conn.setAutoCommit(true);
	// conn.close();
	// return 1;
	// } catch (Exception ex) {
	// try {
	// conn.rollback();
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error(DateUtil.getCurrentDateTime() +
	// "执行batchInsertUserAccount异常，且回滚异常:" + ex.getMessage());
	// return -1;
	// }
	// log.error(DateUtil.getCurrentDateTime() +
	// "执行batchInsertUserAccount异常，回滚成功:" + ex.getMessage());
	// return 0;
	// }
	// }

	public Double findByUserId(List<String> userIds) {
		Double bettingTotalAmount = 0.0;
		Condition userAccountCondition = new Condition(UserAccount.class);
		// 根据分销员ID查询分销员下的顾客量
		userAccountCondition.createCriteria().andCondition("user_id  in", userIds).andCondition("process_type = ", 3);
		// 查询出来该用户的购彩的account然后去订单表里查询购彩金额
		List<UserAccount> userAccountList = userAccountMapper.selectByCondition(userAccountCondition);
		for (int i = 0; i < userAccountList.size(); i++) {
			bettingTotalAmount += userAccountList.get(i).getAmount().doubleValue();
		}
		return -bettingTotalAmount;
	}

	/**
	 * 更新跑马灯中奖信息
	 * 
	 * @param userIdAndRewardList
	 */
	public void updateLotteryWinning(List<UserIdAndRewardDTO> userIdAndRewardList) {
		List<UserIdAndRewardDTO> collect = userIdAndRewardList.stream().filter(u -> u.getReward().doubleValue() > 500).sorted((item1, item2) -> item1.getReward().compareTo(item2.getReward())).collect(Collectors.toList());
		if (collect.size() > 10) {
			collect = collect.subList(0, 10);
		}
		log.info(JSONHelper.bean2json(collect));
		Set<Integer> set = collect.stream().map(dto -> dto.getUserId()).collect(Collectors.toSet());
		log.info("111111111111    " + JSONHelper.bean2json(set));
		List<Integer> userIds = new ArrayList<Integer>(set);
		if (null == userIds || userIds.size() == 0) {
			return;
		}

		List<User> users = userMapper.queryUserByUserIds(userIds);
		Map<Integer, String> map = new HashMap<Integer, String>(users.size());
		for (User user : users) {
			map.put(user.getUserId(), user.getMobile());
		}
		Condition condition = new Condition(LotteryWinningLogTemp.class);
		condition.createCriteria().andCondition("is_show=", 1);
		List<LotteryWinningLogTemp> olds = lotteryWinningLogTempMapper.selectByCondition(condition);
		for (UserIdAndRewardDTO dto : collect) {
			BigDecimal reward = dto.getReward();
			String mobile = map.get(dto.getUserId());
			LotteryWinningLogTemp temp = new LotteryWinningLogTemp();
			temp.setWinningMoney(reward);
			temp.setPhone(mobile == null ? "" : mobile);
			temp.setIsShow(1);
			lotteryWinningLogTempMapper.insert(temp);
		}
		int num = olds.size() + collect.size() - 10;
		if (num > 0) {
			List<Integer> ids = olds.stream().sorted((item1, item2) -> item1.getWinningLogId().compareTo(item2.getWinningLogId())).limit(num).map(dto -> dto.getWinningLogId()).collect(Collectors.toList());
			lotteryWinningLogTempMapper.deleteByLogIds(ids);
		}
	}

	public void updateUserMoneyForCashCoupon(User user) {
		userMapper.updateUserMoneyForCashCoupon(user);
	}

	public int insertUserAccount(UserAccount userAccount) {
		return userAccountMapper.insertUserAccount(userAccount);
	}

	public List<UserAccount> findByUserIdsAndType(List<String> userIds, String data, int i) {
		List<UserAccount> list = userAccountMapper.findByUserIdsAndType(userIds, data, i);
		return list;
	}
}
