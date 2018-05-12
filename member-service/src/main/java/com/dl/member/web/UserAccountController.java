package com.dl.member.web;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.BatchResultDTO;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserAccountCurMonthDTO;
import com.dl.member.dto.UserAccountDTO;
import com.dl.member.dto.UserRechargeDTO;
import com.dl.member.dto.WithdrawalSnDTO;
import com.dl.member.model.UserAccount;
import com.dl.member.param.AmountParam;
import com.dl.member.param.AmountTypeParam;
import com.dl.member.param.MemWithDrawSnParam;
import com.dl.member.param.RecharegeParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.SysConfigParam;
import com.dl.member.param.UpdateUserAccountParam;
import com.dl.member.param.UpdateUserRechargeParam;
import com.dl.member.param.UpdateUserWithdrawParam;
import com.dl.member.param.UserAccountParam;
import com.dl.member.param.UserAccountParamByType;
import com.dl.member.param.UserIdAndRewardListParam;
import com.dl.member.param.UserBonusParam;
import com.dl.member.param.UserWithdrawParam;
import com.dl.member.param.WithDrawParam;
import com.dl.member.service.SysConfigService;
import com.dl.member.service.UserAccountService;
import com.dl.member.service.UserBonusService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    private SysConfigService sysConfigService;
    
	/**
	 * 余额支付引起的账户余额变动
	 * @param SurplusPayParam
	 * @return
	 */	
    @ApiOperation(value="余额支付引起的账户余额变动", notes="余额支付引起的账户余额变动",hidden=false)
    @PostMapping("/changeUserAccountByPay")
    public BaseResult<SurplusPaymentCallbackDTO> addUserAccountByPay(@RequestBody SurplusPayParam surplusPayParam) {
    	BaseResult<SurplusPaymentCallbackDTO> rst = userAccountService.addUserAccountByPay(surplusPayParam);
    	if(rst.getCode() != 0) {
    		return ResultGenerator.genFailResult(rst.getMsg());
    	}
    	SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = rst.getData();
    	return ResultGenerator.genSuccessResult("扣减余额成功",surplusPaymentCallbackDTO);
    }
    
    @ApiOperation(value="余额支付或混合支付失败回滚账户的余额", notes="余额支付或混合支付失败回滚账户的余额",hidden=false)
    @PostMapping("/rollbackUserAccountChangeByPay")
    public BaseResult<SurplusPaymentCallbackDTO> rollbackUserAccountChangeByPay(@RequestBody SurplusPayParam surplusPayParam) {
    	return userAccountService.rollbackUserAccountChangeByPay(surplusPayParam);
    }
    
	/** 
	 * 下单时的账户变动：目前仅红包置为已使用
	 * @param UserBonusParam
	 * @return
	 */
    @ApiOperation(value="下单时的账户变动", notes="下单时的账户变动：目前仅红包置为已使用",hidden=false)
	@RequestMapping(path="/changeUserAccountByCreateOrder", method=RequestMethod.POST)
	public BaseResult<String> changeUserAccountByCreateOrder(@RequestBody UserBonusParam userBonusParam) {
    	return userBonusService.changeUserAccountByCreateOrder(userBonusParam);
	}
	
	/** 
	 * 回滚下单时的账户变动：目前仅红包置为未使用
	 * @param UserBonusParam
	 * @return
	 */
    @ApiOperation(value="回滚下单时的账户变动", notes="回滚下单时的账户变动：目前仅红包置为未使用",hidden=false)
	@RequestMapping(path="/rollbackChangeUserAccountByCreateOrder", method=RequestMethod.POST)
	public BaseResult<String> rollbackChangeUserAccountByCreateOrder(@RequestBody UserBonusParam userBonusParam) {
    	return userBonusService.rollbackChangeUserAccountByCreateOrder(userBonusParam);
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
    
//    @ApiOperation(value="用户生成充值单", notes="用户生成充值单",hidden=false)
//	@RequestMapping(path="/createReCharege", method=RequestMethod.POST)
//    public BaseResult<UserRechargeDTO> createReCharege(@RequestBody AmountParam amountParam){
//    	String rechargeSn = userRechargeService.saveReCharege(amountParam.getAmount());
//    	UserRechargeDTO userRechargeDTO = new UserRechargeDTO();
//    	userRechargeDTO.setRechargeSn(rechargeSn);
//    	return ResultGenerator.genSuccessResult("用户生成充值单成功",userRechargeDTO);
//    }
//    
//    @ApiOperation(value="更新用户充值单", notes="更新用户充值单",hidden=false)
//	@RequestMapping(path="/updateReCharege", method=RequestMethod.POST)
//    public BaseResult<String> updateReCharege(@RequestBody UpdateUserRechargeParam updateUserRechargeParam){
//    	return userRechargeService.updateReCharege(updateUserRechargeParam);
//    }
//    
//    @ApiOperation(value="用户生成提现单", notes="用户生成提现单",hidden=false)
//	@RequestMapping(path="/createUserWithdraw", method=RequestMethod.POST)
//    public BaseResult<WithdrawalSnDTO> createUserWithdraw(@RequestBody UserWithdrawParam userWithdrawParam){
//    	return userWithdrawService.saveWithdraw(userWithdrawParam);
//    }
//    
//    @ApiOperation(value="更新用户提现单", notes="更新用户提现单",hidden=false)
//	@RequestMapping(path="/updateUserWithdraw", method=RequestMethod.POST)
//    public BaseResult<String> updateUserWithdraw(@RequestBody UpdateUserWithdrawParam updateUserWithdrawParam){
//    	return userWithdrawService.updateWithdraw(updateUserWithdrawParam);
//    }
    
    @ApiOperation(value="更新用户账户", notes="更新用户账户",hidden=false)
	@RequestMapping(path="/updateUserAccount", method=RequestMethod.POST)
    public BaseResult<String> updateUserAccount(@RequestBody UpdateUserAccountParam updateUserAccountParam){
    	return userAccountService.updateUserAccount(updateUserAccountParam.getPayId(),updateUserAccountParam.getStatus(),updateUserAccountParam.getAccountSn());
    }
    
    @ApiOperation(value="统计当月的各个用途的资金和", notes="统计当月的各个用途的资金和",hidden=false)
	@RequestMapping(path="/countMoneyCurrentMonth", method=RequestMethod.POST)
    public BaseResult<UserAccountCurMonthDTO> countMoneyCurrentMonth(@RequestBody StrParam strParam){
    	return userAccountService.countMoneyCurrentMonth();
    }
    
	/**
	 * 批量更新用户账户,给派奖使用
	 * @param userAccountByTypeParam
	 * @return
	 */
    @ApiOperation(value="批量更新用户账户", notes="批量更新用户账户",hidden=false)
	@RequestMapping(path="/batchUpdateUserAccount", method=RequestMethod.POST)
    public BaseResult<String> batchUpdateUserAccount(@Valid @RequestBody UserIdAndRewardListParam userIdAndRewardListParam){
    	return userAccountService.batchUpdateUserAccount(userIdAndRewardListParam.getUserIdAndRewardList(),ProjectConstant.REWARD_AUTO);
    }
    
    
	/**
	 * 手动更新用户账户,给后台管理派奖使用
	 * @param userAccountByTypeParam
	 * @return
	 */
    @ApiOperation(value="手动更新用户账户", notes="手动更新用户账户",hidden=false)
	@RequestMapping(path="/manualUpdateUserAccount", method=RequestMethod.POST)
    public BaseResult<String> manualUpdateUserAccount(@Valid @RequestBody UserIdAndRewardListParam userIdAndRewardListParam){
    	return userAccountService.batchUpdateUserAccount(userIdAndRewardListParam.getUserIdAndRewardList(),ProjectConstant.REWARD_MANUAL);
    }
    
    
    @ApiOperation(value="充值", notes="充值",hidden=false)
	@RequestMapping(path="/rechargeUserMoneyLimit", method=RequestMethod.POST)
    public BaseResult<String> rechargeUserMoneyLimit(@Valid @RequestBody RecharegeParam recharegeParam){
    	return userAccountService.rechargeUserMoneyLimit(recharegeParam);
    }
    
    @ApiOperation(value="提现", notes="提现",hidden=false)
	@RequestMapping(path="/withdrawUserMoney", method=RequestMethod.POST)
    public BaseResult<String> withdrawUserMoney(@Valid @RequestBody WithDrawParam withdrawParam){
    	return userAccountService.withdrawUserMoney(withdrawParam);
    }    
    
	/**
	 * 给第三方支付成功后提供的记录账户流水
	 * @param userAccountByTypeParam
	 * @return
	 */
    @ApiOperation(value="给单纯第三方支付成功后提供的记录账户流水", notes="给第三方支付成功后提供的记录账户流水",hidden=false)
	@RequestMapping(path="/insertUserAccount", method=RequestMethod.POST)
    public BaseResult<String> insertUserAccount(@Valid @RequestBody UserAccountParamByType userAccountParamByType){
    	return ResultGenerator.genSuccessResult("生成账户流水成功",userAccountService.saveUserAccountForThirdPay(userAccountParamByType));
    }
    
	/**
	 * 查询业务值的限制：CommonConstants 中9-派奖限制 8-提现限制
	 * @return
	 */
    @ApiOperation(value="查询业务值的限制", notes="查询业务值的限制",hidden=false)
	@RequestMapping(path="/queryBusinessLimit", method=RequestMethod.POST)
	public BaseResult<SysConfigDTO> queryBusinessLimit(@Valid @RequestBody SysConfigParam sysConfigParam) {
		SysConfigDTO sysDTO = sysConfigService.querySysConfig(sysConfigParam.getBusinessId());
		return  ResultGenerator.genSuccessResult("查询业务值的限制",sysDTO);
	}
    
    /**
     * 提现失败回滚账户可提现余额
     * @param MemWithDrawSnParam memWithDrawSnParam
     */
    @ApiOperation(value="提现失败回滚账户可提现余额", notes="提现失败回滚账户可提现余额",hidden=false)
	@RequestMapping(path="/rollbackUserMoneyWithDrawFailure", method=RequestMethod.POST)
    public BaseResult<SurplusPaymentCallbackDTO> rollbackUserMoneyWithDrawFailure(@Valid @RequestBody MemWithDrawSnParam memWithDrawSnParam){
    	return userAccountService.rollbackUserMoneyWithDrawFailure(memWithDrawSnParam);
    }
    
}
