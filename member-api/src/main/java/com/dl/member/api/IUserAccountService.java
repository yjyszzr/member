package com.dl.member.api;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserRechargeDTO;
import com.dl.member.dto.UserWithdrawDTO;
import com.dl.member.param.AmountParam;
import com.dl.member.param.MemRollParam;
import com.dl.member.param.MemWithDrawSnParam;
import com.dl.member.param.RecharegeParam;
//import com.pgt.shop.member.dto.UserCapitalDTO;
//import com.pgt.shop.member.param.CancelChangeParam;
//import com.pgt.shop.member.param.ConfirmOrderParam;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.SysConfigParam;
import com.dl.member.param.UpdateUserAccountParam;
import com.dl.member.param.UpdateUserRechargeParam;
import com.dl.member.param.UpdateUserWithdrawParam;
import com.dl.member.param.UserAccountParam;
import com.dl.member.param.UserAccountParamByType;
import com.dl.member.param.UserBonusParam;
import com.dl.member.param.UserIdAndRewardListParam;
import com.dl.member.param.UserParam;
import com.dl.member.param.UserWithdrawParam;
//import com.pgt.shop.member.param.UserCapitalParam;
//import com.pgt.shop.member.param.UserRefundParam;
import com.dl.member.param.WithDrawParam;

/**
 * 用户账户接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IUserAccountService {
	
	/**
	 * 下单时的账户变动：红包置为已使用。
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/account/changeUserAccountByCreateOrder", method=RequestMethod.POST)
	public BaseResult<String> changeUserAccountByCreateOrder(UserBonusParam userbonusParam);
	
	/**
	 * 下单失败账户的回滚:红包的回滚。
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/account/rollbackChangeUserAccountByCreateOrder", method=RequestMethod.POST)
	public BaseResult<String> rollbackChangeUserAccountByCreateOrder(UserBonusParam userbonusParam);
	
	/**
	 * 付款完成的账户变动：扣减余额
	 * @param SurplusPayParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/changeUserAccountByPay", method=RequestMethod.POST)
	public BaseResult<SurplusPaymentCallbackDTO> changeUserAccountByPay(SurplusPayParam surplusPayParam);
	
	
	/**
	 * 回滚 付款完成的账户变动：余额回滚
	 * @param SurplusPayParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/rollbackUserAccountChangeByPay", method=RequestMethod.POST)
	public BaseResult<SurplusPaymentCallbackDTO> rollbackUserAccountChangeByPay(SurplusPayParam surplusPayParam);
	
	/**
	 * 用户生成充值单
	 * @param amountParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/createReCharege", method=RequestMethod.POST)
	public BaseResult<UserRechargeDTO> createReCharege(@RequestBody AmountParam amountParam);
	
	/**
	 * 更新用户充值单
	 * @param userRechargeParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/updateReCharege", method=RequestMethod.POST)
	public BaseResult<String> updateReCharege(@RequestBody UpdateUserRechargeParam updateUserRechargeParam);
	
	/**
	 * 用户生成提现单
	 * @param updateUserWithdrawParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/createUserWithdraw", method=RequestMethod.POST)
	public BaseResult<UserWithdrawDTO> createUserWithdraw(@RequestBody UserWithdrawParam userWithdrawParam);
	
	/**
	 * 活动收益提取
	 * @param amountParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/activityRewardUserMoney", method=RequestMethod.POST)
	public BaseResult<String> activityRewardUserMoney(@RequestBody RecharegeParam recharegeParam);
	
	
	/**
	 * 更新用户提现单
	 * @param updateUserWithdrawParam
	 * @return
	 */	
	@RequestMapping(path="/user/account/updateUserWithdraw", method=RequestMethod.POST)
	public BaseResult<String> updateUserWithdraw(@RequestBody UpdateUserWithdrawParam updateUserWithdrawParam);
	
	/**
	 * 更新用户账户
	 * @param updateUserAccountParam
	 * @return
	 */
	@RequestMapping(path="/user/account/updateUserAccount", method=RequestMethod.POST)
    public BaseResult<String> updateUserAccount(@RequestBody UpdateUserAccountParam updateUserAccountParam);
	
	/**
	 * 内部使用的用户资金变动服务：目前仅提供给派奖使用
	 * @param updateUserAccountParam
	 * @return
	 */
	@RequestMapping(path="/user/account/batchUpdateUserAccount", method=RequestMethod.POST)
    public BaseResult<String> changeUserAccountByType(@Valid @RequestBody UserIdAndRewardListParam userAccountByTypeParam);
	
	@RequestMapping(path="/user/account/rollbackUserMoneyWithDrawFailure", method=RequestMethod.POST)
    public BaseResult<SurplusPaymentCallbackDTO> rollbackUserMoneyWithDrawFailure(@RequestBody MemWithDrawSnParam memWithDrawSnParam);
	
	@RequestMapping(path="/user/account/rollbackUserMoney", method=RequestMethod.POST)
    public BaseResult<SurplusPaymentCallbackDTO> rollbackUserMoneyFailure(@RequestBody MemRollParam memRollParam);
	
	/**
	 * 给第三方支付提供的记录账户流水
	 * @param userAccountByTypeParam
	 * @return
	 */
	@RequestMapping(path="/user/account/insertUserAccount", method=RequestMethod.POST)
    public BaseResult<String> insertUserAccount(@Valid @RequestBody UserAccountParamByType userAccountParamByType);
	
	
    /**
     * 提现
     * @param withdrawParam
     * @return
     */
	@RequestMapping(path="/user/account/withdrawUserMoney", method=RequestMethod.POST)
    public BaseResult<String> withdrawUserMoney(@Valid @RequestBody WithDrawParam withdrawParam);

	/**
	 * 充值
	 * @param recharegeParam
	 * @return
	 */
	@RequestMapping(path="/user/account/rechargeUserMoneyLimit", method=RequestMethod.POST)
    public BaseResult<String> rechargeUserMoneyLimit(@Valid @RequestBody RecharegeParam recharegeParam);
	
	/**
	 * 查询业务阈值
	 * @param recharegeParam
	 * @return
	 */
	@RequestMapping(path="/user/account/queryBusinessLimit", method=RequestMethod.POST)
	public BaseResult<SysConfigDTO> queryBusinessLimit(@Valid @RequestBody SysConfigParam sysConfigParam);
	
	
	@RequestMapping(path="/user/account/updateUserMoneyAndUserMoneyLimit", method=RequestMethod.POST)
	public BaseResult<Integer> updateUserMoneyAndUserMoneyLimit(@Valid @RequestBody UserParam _user); 
	
	@RequestMapping(path="/user/account/insertUserAccountBySelective", method=RequestMethod.POST)
	public BaseResult<Integer> insertUserAccountBySelective(@Valid @RequestBody UserAccountParam userAccountParam);
	
	//	
//	/**
//	 * 未付款时取消订单引起的账户变动：包括积分和红包
//	 * @param OrderParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/changeUserAccountByCancel", method=RequestMethod.POST)
//	public BaseResult<String> changeUserAccountByCancel(CancelChangeParam cancelChangeParam);

//	/**
//	 * 回滚 未付款时取消订单引起的账户变动：包括积分和红包
//	 * @param CancelChangeParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/rollbackChangeUserAccountByCancel", method=RequestMethod.POST)
//	public BaseResult<String> rollbackChangeUserAccountByCancel(CancelChangeParam cancelChangeParam);
//	
//	/**
//	 * 确认收货引起的账户变动：包括积分
//	 * @param OrderParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/changeUserAccountByConfirm", method=RequestMethod.POST)
//	public BaseResult<String> changeUserAccountByConfirm(ConfirmOrderParam confirmOrderParam);
//
//	/**
//	 * 回滚 未确认收货引起的账户变动：包括积分
//	 * @param CancelChangeParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/rollbackChangeUserAccountByConfirm", method=RequestMethod.POST)
//	public BaseResult<String> rollbackChangeUserAccountByConfirm(ConfirmOrderParam confirmOrderParam);	
//	
//	/**
//	 * 用户申请提现接口
//	 * @param userRefundParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/applyWithdraw", method=RequestMethod.POST)
//	public BaseResult<UserCapitalDTO> applyWithdraw(@RequestBody UserCapitalParam userCapitalParam);	
//	
//	/**
//	 * 用户提现回调方法
//	 * @param userRefundParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/rollbackWithdrawByConfirm", method=RequestMethod.POST)
//	public BaseResult<String> rollbackWithdrawByConfirm(@RequestBody UserCapitalParam userCapitalParam);	
//	
//	/**
//	 * 回滚：用户提现回调失败，进行状态修改
//	 * @param userRefundParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/rollbackWithdrawByCancel", method=RequestMethod.POST)
//	public BaseResult<String> rollbackWithdrawByCancel(@RequestBody UserCapitalParam userCapitalParam);	
//	
//	/**
//	 * 退款回调方法
//	 * @param userRefundParam
//	 * @return
//	 */	
//	@RequestMapping(path="/user/account/rollbackRefundByConfirm", method=RequestMethod.POST)
//	public BaseResult<String> rollbackRefundByConfirm(@RequestBody UserRefundParam userRefundParam);	
}
