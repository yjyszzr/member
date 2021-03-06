package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserAccountByTimeDTO;
import com.dl.member.dto.UserAccountCurMonthDTO;
import com.dl.member.dto.UserAccountDTO;
import com.dl.member.dto.UserAccountListAndCountDTO;
import com.dl.member.dto.UserIdAndRewardDTO;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.param.AmountTypeParam;
import com.dl.member.param.MemRollParam;
import com.dl.member.param.MemWithDrawSnParam;
import com.dl.member.param.RecharegeParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.SysConfigParam;
import com.dl.member.param.TimeTypeParam;
import com.dl.member.param.UpdateUserAccountParam;
import com.dl.member.param.UserAccountParam;
import com.dl.member.param.UserAccountParamByType;
import com.dl.member.param.UserBonusParam;
import com.dl.member.param.UserIdAndRewardListParam;
import com.dl.member.param.UserParam;
import com.dl.member.param.WithDrawParam;
import com.dl.member.service.SysConfigService;
import com.dl.member.service.UserAccountService;
import com.dl.member.service.UserBonusService;
import com.dl.member.service.UserService;
import com.github.pagehelper.PageInfo;

/**
 * Created by zhangzirong on 2018/03/15.
 */
@RestController
@RequestMapping("/user/account")
public class UserAccountController {
	@Resource
	private UserAccountService userAccountService;

	@Resource
	private UserBonusService userBonusService;

	@Resource
	private UserService userService;

	@Resource
	private SysConfigService sysConfigService;

	/**
	 * 余额支付引起的账户余额变动
	 * 
	 * @param SurplusPayParam
	 * @return
	 */
	@ApiOperation(value = "余额支付引起的账户余额变动", notes = "余额支付引起的账户余额变动", hidden = false)
	@PostMapping("/changeUserAccountByPay")
	public BaseResult<SurplusPaymentCallbackDTO> addUserAccountByPay(@RequestBody SurplusPayParam surplusPayParam) {
		BaseResult<SurplusPaymentCallbackDTO> rst = userAccountService.addUserAccountByPay(surplusPayParam);
		if (rst.getCode() != 0) {
			return ResultGenerator.genFailResult(rst.getMsg());
		}
		SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = rst.getData();
		return ResultGenerator.genSuccessResult("扣减余额成功", surplusPaymentCallbackDTO);
	}

	@ApiOperation(value = "余额支付或混合支付失败回滚账户的余额", notes = "余额支付或混合支付失败回滚账户的余额", hidden = false)
	@PostMapping("/rollbackUserAccountChangeByPay")
	public BaseResult<SurplusPaymentCallbackDTO> rollbackUserAccountChangeByPay(@RequestBody SurplusPayParam surplusPayParam) {
		return userAccountService.rollbackUserAccountChangeByPay(surplusPayParam);
	}

	/**
	 * 下单时的账户变动：目前仅红包置为已使用
	 * 
	 * @param UserBonusParam
	 * @return
	 */
	@ApiOperation(value = "下单时的账户变动", notes = "下单时的账户变动：目前仅红包置为已使用", hidden = false)
	@RequestMapping(path = "/changeUserAccountByCreateOrder", method = RequestMethod.POST)
	public BaseResult<String> changeUserAccountByCreateOrder(@RequestBody UserBonusParam userBonusParam) {
		return userBonusService.changeUserAccountByCreateOrder(userBonusParam);
	}

	/**
	 * 回滚下单时的账户变动：目前仅红包置为未使用
	 * 
	 * @param UserBonusParam
	 * @return
	 */
	@ApiOperation(value = "回滚下单时的账户变动", notes = "回滚下单时的账户变动：目前仅红包置为未使用", hidden = false)
	@RequestMapping(path = "/rollbackChangeUserAccountByCreateOrder", method = RequestMethod.POST)
	public BaseResult<String> rollbackChangeUserAccountByCreateOrder(@RequestBody UserBonusParam userBonusParam) {
		return userBonusService.rollbackChangeUserAccountByCreateOrder(userBonusParam);
	}

	/**
	 * 查询用户账户明细列表和统计数据
	 * 
	 * @param UserBonusParam
	 * @return
	 */
	@ApiOperation(value = "查询用户账户明细列表和统计数据", notes = "查询用户账户明细列表和统计数据", hidden = false)
	@RequestMapping(path = "/getUserAccountListAndCountTotal", method = RequestMethod.POST)
	public BaseResult<UserAccountListAndCountDTO> getUserAccountListAndCountTotal(@RequestBody AmountTypeParam amountTypeParam) {
		return userAccountService.getUserAccountListAndCountTotal(amountTypeParam);
	}
	
	/** 
	 * 查询用户账户明细列表
	 * @param UserBonusParam
	 * @return
	 */
    @ApiOperation(value="查询用户账户明细列表", notes="查询用户账户明细列表",hidden=false)
	@RequestMapping(path="/getUserAccountList", method=RequestMethod.POST)
	public BaseResult<PageInfo<UserAccountDTO>> getUserAccountList(@RequestBody AmountTypeParam amountTypeParam) {
    	PageInfo<UserAccountDTO> rst = userAccountService.getUserAccountList(Integer.valueOf(amountTypeParam.getAmountType()),amountTypeParam.getPageNum(), amountTypeParam.getPageSize()); 
    	return ResultGenerator.genSuccessResult("查询用户账户明细列表",rst);
	}

	// @ApiOperation(value="用户生成充值单", notes="用户生成充值单",hidden=false)
	// @RequestMapping(path="/createReCharege", method=RequestMethod.POST)
	// public BaseResult<UserRechargeDTO> createReCharege(@RequestBody
	// AmountParam amountParam){
	// String rechargeSn =
	// userRechargeService.saveReCharege(amountParam.getAmount());
	// UserRechargeDTO userRechargeDTO = new UserRechargeDTO();
	// userRechargeDTO.setRechargeSn(rechargeSn);
	// return ResultGenerator.genSuccessResult("用户生成充值单成功",userRechargeDTO);
	// }
	//
	// @ApiOperation(value="更新用户充值单", notes="更新用户充值单",hidden=false)
	// @RequestMapping(path="/updateReCharege", method=RequestMethod.POST)
	// public BaseResult<String> updateReCharege(@RequestBody
	// UpdateUserRechargeParam updateUserRechargeParam){
	// return userRechargeService.updateReCharege(updateUserRechargeParam);
	// }
	//
	// @ApiOperation(value="用户生成提现单", notes="用户生成提现单",hidden=false)
	// @RequestMapping(path="/createUserWithdraw", method=RequestMethod.POST)
	// public BaseResult<WithdrawalSnDTO> createUserWithdraw(@RequestBody
	// UserWithdrawParam userWithdrawParam){
	// return userWithdrawService.saveWithdraw(userWithdrawParam);
	// }
	//
	// @ApiOperation(value="更新用户提现单", notes="更新用户提现单",hidden=false)
	// @RequestMapping(path="/updateUserWithdraw", method=RequestMethod.POST)
	// public BaseResult<String> updateUserWithdraw(@RequestBody
	// UpdateUserWithdrawParam updateUserWithdrawParam){
	// return userWithdrawService.updateWithdraw(updateUserWithdrawParam);
	// }

	@ApiOperation(value = "更新用户账户", notes = "更新用户账户", hidden = false)
	@RequestMapping(path = "/updateUserAccount", method = RequestMethod.POST)
	public BaseResult<String> updateUserAccount(@RequestBody UpdateUserAccountParam updateUserAccountParam) {
		return userAccountService.updateUserAccount(updateUserAccountParam.getPayId(), updateUserAccountParam.getStatus(), updateUserAccountParam.getAccountSn());
	}

    @ApiOperation(value="统计当月的各个用途的资金和", notes="统计当月的各个用途的资金和",hidden=false)
	@RequestMapping(path="/countMoneyCurrentMonth", method=RequestMethod.POST)
    public BaseResult<UserAccountCurMonthDTO> countMoneyCurrentMonth(@RequestBody StrParam strParam){
    	return userAccountService.countMoneyCurrentMonth();
    }

	/**
	 * 批量更新用户账户,给派奖使用
	 * 
	 * @param userAccountByTypeParam
	 * @return
	 */
	@ApiOperation(value = "批量更新用户账户", notes = "批量更新用户账户", hidden = false)
	@RequestMapping(path = "/batchUpdateUserAccount", method = RequestMethod.POST)
	public BaseResult<String> batchUpdateUserAccount(@Valid @RequestBody UserIdAndRewardListParam userIdAndRewardListParam) {
		List<UserIdAndRewardDTO> userIdAndRewardList = userIdAndRewardListParam.getUserIdAndRewardList();
		BaseResult<String> batchUpdateUserAccount = userAccountService.batchUpdateUserAccount(userIdAndRewardList, ProjectConstant.REWARD_AUTO);
		return batchUpdateUserAccount;
	}

	/**
	 * 手动更新用户账户,给后台管理派奖使用
	 * 
	 * @param userAccountByTypeParam
	 * @return
	 */
	@ApiOperation(value = "手动更新用户账户", notes = "手动更新用户账户", hidden = false)
	@RequestMapping(path = "/manualUpdateUserAccount", method = RequestMethod.POST)
	public BaseResult<String> manualUpdateUserAccount(@Valid @RequestBody UserIdAndRewardListParam userIdAndRewardListParam) {
		return userAccountService.batchUpdateUserAccount(userIdAndRewardListParam.getUserIdAndRewardList(), ProjectConstant.REWARD_MANUAL);
	}

	/**
	 * 针对答题竞猜给用户派奖
	 * 
	 * @param userIdAndRewardListParam
	 * @return
	 */
	@ApiOperation(value = "派奖到用户不可提现余额", notes = "派奖到用户不可提现余额", hidden = false)
	@RequestMapping(path = "/batchUpdateUserRewardToUserMoneyLimit", method = RequestMethod.POST)
	public BaseResult<String> batchUpdateUserRewardToUserMoneyLimit(@Valid @RequestBody UserIdAndRewardListParam userIdAndRewardListParam) {
		return userAccountService.batchUpdateUserRewardToUserMoneyLimit(userIdAndRewardListParam.getUserIdAndRewardList());
	}

	@ApiOperation(value = "充值", notes = "充值", hidden = false)
	@RequestMapping(path = "/rechargeUserMoneyLimit", method = RequestMethod.POST)
	public BaseResult<String> rechargeUserMoneyLimit(@Valid @RequestBody RecharegeParam recharegeParam) {
		return userAccountService.rechargeUserMoneyLimit(recharegeParam);
	}

	@ApiOperation(value = "提现", notes = "提现", hidden = false)
	@RequestMapping(path = "/withdrawUserMoney", method = RequestMethod.POST)
	public BaseResult<String> withdrawUserMoney(@Valid @RequestBody WithDrawParam withdrawParam) {
		return userAccountService.withdrawUserMoney(withdrawParam);
	}
	@ApiOperation(value = "活动收益提取", notes = "活动收益提取", hidden = false)
	@RequestMapping(path = "/activityRewardUserMoney", method = RequestMethod.POST)
	public BaseResult<String> activityRewardUserMoney(@Valid @RequestBody RecharegeParam recharegeParam) {
		return userAccountService.activityRewardUserMoney(recharegeParam);
	}
	/**
	 * 给第三方支付成功后提供的记录账户流水
	 * 
	 * @param userAccountByTypeParam
	 * @return
	 */
	@ApiOperation(value = "给单纯第三方支付成功后提供的记录账户流水", notes = "给第三方支付成功后提供的记录账户流水", hidden = false)
	@RequestMapping(path = "/insertUserAccount", method = RequestMethod.POST)
	public BaseResult<String> insertUserAccount(@Valid @RequestBody UserAccountParamByType userAccountParamByType) {
		return ResultGenerator.genSuccessResult("生成账户流水成功", userAccountService.saveUserAccountForThirdPay(userAccountParamByType));
	}

	/**
	 * 查询业务值的限制：CommonConstants 中9-派奖限制 8-提现限制
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询业务值的限制", notes = "查询业务值的限制", hidden = false)
	@RequestMapping(path = "/queryBusinessLimit", method = RequestMethod.POST)
	public BaseResult<SysConfigDTO> queryBusinessLimit(@Valid @RequestBody SysConfigParam sysConfigParam) {
		SysConfigDTO sysDTO = sysConfigService.querySysConfig(sysConfigParam.getBusinessId());
		return ResultGenerator.genSuccessResult("查询业务值的限制", sysDTO);
	}

	/**
	 * 提现失败回滚账户可提现余额
	 * 
	 * @param MemWithDrawSnParam
	 *            memWithDrawSnParam
	 */
	@ApiOperation(value = "提现失败回滚账户可提现余额", notes = "提现失败回滚账户可提现余额", hidden = false)
	@RequestMapping(path = "/rollbackUserMoneyWithDrawFailure", method = RequestMethod.POST)
	public BaseResult<SurplusPaymentCallbackDTO> rollbackUserMoneyWithDrawFailure(@Valid @RequestBody MemWithDrawSnParam memWithDrawSnParam) {
		return userAccountService.rollbackUserMoneyWithDrawFailure(memWithDrawSnParam);
	}

	/***
	 * 出票失败，回滚到可提现金额中
	 * 
	 * @param memWithDrawSnParam
	 * @return
	 */
	@ApiOperation(value = "出票失败，回滚到可提现金额中", notes = "出票失败，回滚到可提现金额中", hidden = false)
	@RequestMapping(path = "/rollbackUserMoney", method = RequestMethod.POST)
	public BaseResult<Object> rollbackUserMoneyOrderFailure(@Valid @RequestBody MemRollParam memRollParam) {
		return userAccountService.rollbackUserMoneyOrderFailure(memRollParam);
	}
	
//	@ApiOperation(value = "出票失败，回滚到可提现金额中", notes = "出票失败，回滚到可提现金额中", hidden = false)
	@RequestMapping(path = "/updateUserMoneyAndUserMoneyLimit", method = RequestMethod.POST)
	public  BaseResult<Integer> updateUserMoneyAndUserMoneyLimit(@Valid @RequestBody UserParam _user) {
		int tag = userAccountService.updateUserMoneyAndUserMoneyLimit(_user);
		return ResultGenerator.genSuccessResult("扣款", Integer.valueOf(tag));
	}
	
//	@ApiOperation(value = "出票失败，回滚到可提现金额中", notes = "出票失败，回滚到可提现金额中", hidden = false)
	@RequestMapping(path = "/insertUserAccountBySelective", method = RequestMethod.POST)
	public BaseResult<Integer>  insertUserAccountBySelective(@Valid @RequestBody UserAccountParam userAccountParam) {
		int tag = userAccountService.insertUserAccountBySelective(userAccountParam);
		return ResultGenerator.genSuccessResult("记流水", Integer.valueOf(tag));
	}
	
}
