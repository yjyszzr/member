package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.UserAccountDTO;
import com.dl.member.dto.UserRechargeDTO;
import com.dl.member.dto.UserWithdrawDTO;
import com.dl.member.param.AmountParam;
import com.dl.member.param.PageParam;
import com.dl.member.param.RollackSurplusPayParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.SurplusPayParam;
import com.dl.member.param.UpdateUserRechargeParam;
import com.dl.member.param.UpdateUserWithdrawParam;
import com.dl.member.param.UserBonusParam;
import com.dl.member.param.UserWithdrawParam;
import com.dl.member.service.UserAccountService;
import com.dl.member.service.UserBonusService;
import com.dl.member.service.UserRechargeService;
import com.dl.member.service.UserWithdrawService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

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
    private UserRechargeService userRechargeService;
    
    @Resource
    private UserWithdrawService userWithdrawService;
    
	/**
	 * 余额支付引起的账户余额变动
	 * @param SurplusPayParam
	 * @return
	 */	
    @ApiOperation(value="余额支付引起的账户余额变动", notes="余额支付引起的账户余额变动",hidden=false)
    @PostMapping("/addUserAccountByPay")
    public BaseResult<SurplusPaymentCallbackDTO> addUserAccountByPay(@RequestBody SurplusPayParam surplusPayParam) {
    	BaseResult<SurplusPaymentCallbackDTO> rst = userAccountService.addUserAccountByPay(surplusPayParam);
    	if(rst.getCode() == 0) {
    		return ResultGenerator.genFailResult(rst.getMsg());
    	}
    	SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = rst.getData();
    	return ResultGenerator.genSuccessResult("扣减余额成功",surplusPaymentCallbackDTO);
    }
    
    @ApiOperation(value="回滚账户的余额和订单的余额", notes="回滚账户的余额和订单的余额",hidden=false)
    @PostMapping("/rollbackUserAccountChangeByPay")
    public BaseResult<SurplusPaymentCallbackDTO> rollbackUserAccountChangeByPay(@RequestBody SurplusPayParam surplusPayParam) {
    	SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = userAccountService.rollbackUserAccountChangeByPay(surplusPayParam);
    	return ResultGenerator.genSuccessResult("回滚扣减余额成功",surplusPaymentCallbackDTO);
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
	public BaseResult<PageInfo<UserAccountDTO>> getUserAccountList(@RequestBody PageParam pageParam) {
    	PageInfo<UserAccountDTO> rst = userAccountService.getUserAccountList(null,pageParam.getPageNum(), pageParam.getPageSize()); 
    	return ResultGenerator.genSuccessResult("查询用户账户明细列表",rst);
	}
    
    @ApiOperation(value="用户生成充值单", notes="用户生成充值单",hidden=false)
	@RequestMapping(path="/createReCharege", method=RequestMethod.POST)
    public BaseResult<UserRechargeDTO> createReCharege(@RequestBody AmountParam amountParam){
    	String rechargeSn = userRechargeService.saveReCharege(amountParam.getAmount());
    	UserRechargeDTO userRechargeDTO = new UserRechargeDTO();
    	userRechargeDTO.setRechargeSn(rechargeSn);
    	return ResultGenerator.genSuccessResult("用户生成充值单成功",userRechargeDTO);
    }
    
    @ApiOperation(value="更新用户充值单", notes="更新用户充值单",hidden=false)
	@RequestMapping(path="/updateReCharege", method=RequestMethod.POST)
    public BaseResult<String> updateReCharege(@RequestBody UpdateUserRechargeParam updateUserRechargeParam){
    	return userRechargeService.updateReCharege(updateUserRechargeParam);
    }
    
    @ApiOperation(value="用户生成提现单", notes="用户生成提现单",hidden=false)
	@RequestMapping(path="/createUserWithdraw", method=RequestMethod.POST)
    public BaseResult<UserWithdrawDTO> createUserWithdraw(@RequestBody UserWithdrawParam userWithdrawParam){
    	String withDrawSn = userWithdrawService.saveWithdraw(userWithdrawParam);
    	UserWithdrawDTO userWithdrawDTO = new UserWithdrawDTO();
    	userWithdrawDTO.setWithdrawalSn(withDrawSn);
    	return ResultGenerator.genSuccessResult("用户生成提现单成功",userWithdrawDTO);
    }
    
    @ApiOperation(value="更新用户提现单", notes="更新用户提现单",hidden=false)
	@RequestMapping(path="/updateUserWithdraw", method=RequestMethod.POST)
    public BaseResult<String> updateUserWithdraw(@RequestBody UpdateUserWithdrawParam updateUserWithdrawParam){
    	return userWithdrawService.updateWithdraw(updateUserWithdrawParam);
    	
    }
    
    
    
}
