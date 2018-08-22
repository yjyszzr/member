package com.dl.member.web;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.DonationPriceDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.PayLogIdParam;
import com.dl.member.param.UserBonusIdParam;
import com.dl.member.param.UserBonusStatusParam;
import com.dl.member.service.UserBonusService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

/**
* Created by zhangzirong on 2018/03/15.
*/
@RestController
@RequestMapping("/user/bonus")
public class UserBonusController {
    @Resource
    private UserBonusService userBonusService;
    
    @ApiOperation(value="根据userBonusId查询单个红包", notes="根据userBonusId查询单个红包",hidden=false)
    @PostMapping("/queryUserBonus")
    public BaseResult<UserBonusDTO> queryUserBonus(@RequestBody UserBonusIdParam userBonusIdParam) {
    	UserBonusDTO userBonusDTO = userBonusService.queryUserBonus(userBonusIdParam.getUserBonusId());
    	if(null == userBonusDTO) {
    		return ResultGenerator.genFailResult("未查询到userBonusId="+userBonusIdParam+"的红包");
    	}
    	return ResultGenerator.genSuccessResult("根据userBonusId查询单个红包成功",userBonusDTO);
    }
    
    @ApiOperation(value="根据状态查询有效的红包集合", notes="根据状态查询有效的红包集合",hidden=false)
    @PostMapping("/queryBonusListByStatus")
    public BaseResult<PageInfo<UserBonusDTO>> queryBonusListByStatus(@RequestBody UserBonusStatusParam userBonusStatusParam) {
    	PageInfo<UserBonusDTO> userBonusDTOList =  userBonusService.queryBonusListByStatus(userBonusStatusParam.getStatus(),userBonusStatusParam.getPageNum(),userBonusStatusParam.getPageSize());
    	return ResultGenerator.genSuccessResult("根据状态查询有效的红包集合成功",userBonusDTOList);
    }
    
    @ApiOperation(value="支付的时候查询用户可用的红包列表", notes="支付的时候查询用户可用的红包列表",hidden=false)
    @PostMapping("/queryValidBonusList")
    public BaseResult<List<UserBonusDTO>> queryValidBonusList(@RequestBody BonusLimitConditionParam bonusLimitConditionParam) {
    	List<UserBonusDTO> userBonusDTOList =  userBonusService.queryValidBonusListForPay(bonusLimitConditionParam);
    	return ResultGenerator.genSuccessResult("查询用户有效的红包列表成功",userBonusDTOList);
    }
    
    @ApiOperation(value="领取充值后 弹出的赠送金额", notes="领取充值后 弹出的赠送金额",hidden=false)
    @PostMapping("/rechargeSucReiceiveBonus")
    public BaseResult<DonationPriceDTO> rechargeSucReiceiveBonus(@RequestBody PayLogIdParam payLogIdParam) {
    	return userBonusService.receiveRechargeUserBonusStr(Integer.valueOf(payLogIdParam.getPayLogId()));
    }
    
    @ApiOperation(value="领取充值送随机红包", notes="充值成功后领取充值送随机红包",hidden=false)
    @PostMapping("/reiceiveBonusAfterRecharge")
    public BaseResult<DonationPriceDTO> reiceiveBonusAfterRecharge(@RequestBody PayLogIdParam payLogIdParam) {
    	return userBonusService.receiveRechargeUserBonus(Integer.valueOf(payLogIdParam.getPayLogId()));
    }
    
    @ApiOperation(value="领取充值送随机红包20180822新增", notes="充值成功后领取充值送随机红包20180822新增",hidden=false)
    @PostMapping("/reiceiveBonusAfterRechargeNew")
    public BaseResult<DonationPriceDTO> reiceiveBonusAfterRechargeNew(@RequestBody PayLogIdParam payLogIdParam) {
    	return userBonusService.receiveRechargeUserBonusNew(Integer.valueOf(payLogIdParam.getPayLogId()));
    }
    
    @ApiOperation(value="更新红包的过期状态", notes="更新红包的过期状态",hidden=false)
	@RequestMapping(path="/updateBonusExpire", method=RequestMethod.POST)
	public BaseResult<String> updateBonusExpire(@RequestBody EmptyParam emptyParam) {
    	userBonusService.updateBonusExpire();
		return ResultGenerator.genSuccessResult("更新过期红包成功");
	}
    
}
