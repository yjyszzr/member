package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
//import com.pgt.shop.member.dto.UserCapitalDTO;
//import com.pgt.shop.member.param.CancelChangeParam;
//import com.pgt.shop.member.param.ConfirmOrderParam;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.UserBonusParam;
//import com.pgt.shop.member.param.UserCapitalParam;
//import com.pgt.shop.member.param.UserRefundParam;

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
