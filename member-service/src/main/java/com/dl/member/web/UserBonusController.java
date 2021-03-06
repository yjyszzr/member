package com.dl.member.web;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.DonationPriceDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.model.UserBonus;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.PayLogIdParam;
import com.dl.member.param.UserBonusIdParam;
import com.dl.member.param.UserBonusStatusParam;
import com.dl.member.service.UserBonusService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;

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
    
    @ApiOperation(value="根据userId查询有效红包数量和总金额", notes="根据userId查询有效红包数量和总金额",hidden=false)
    @PostMapping("/queryUserBonusNumAndPrice")
    public BaseResult<UserBonusDTO> queryUserBonusNumAndPrice(@RequestBody UserBonusIdParam userBonusIdParam) {
    	//参数中存放userid字段节用
    	UserBonusDTO userBonusDTO = userBonusService.getBonusByUserId(userBonusIdParam.getUserBonusId());
    	if(null == userBonusDTO) {
    		return ResultGenerator.genFailResult("未查询到user_id="+userBonusIdParam+"的红包");
    	}
    	return ResultGenerator.genSuccessResult("根据user_id查询红包成功",userBonusDTO);
    }
    
    @ApiOperation(value="根据状态查询有效的红包集合", notes="根据状态查询有效的红包集合",hidden=false)
    @PostMapping("/queryBonusListByStatus")
    public BaseResult<PageInfo<UserBonusDTO>> queryBonusListByStatus(@RequestBody UserBonusStatusParam userBonusStatusParam) {
    	PageInfo<UserBonusDTO> userBonusDTOList =  userBonusService.queryBonusListByStatus(userBonusStatusParam.getStatus(),userBonusStatusParam.getPageNum(),userBonusStatusParam.getPageSize());
    	return ResultGenerator.genSuccessResult("根据状态查询有效的红包集合成功",userBonusDTOList);
    }
    
    @ApiOperation(value="支付的时候查询用户可用的红包列表: 内部接口", notes="支付的时候查询用户可用的红包列表：内部接口",hidden=false)
    @PostMapping("/queryValidBonusList")
    public BaseResult<List<UserBonusDTO>> queryValidBonusList(@RequestBody BonusLimitConditionParam bonusLimitConditionParam) {
    	List<UserBonusDTO> userBonusDTOList =  userBonusService.queryValidBonusListForPay(bonusLimitConditionParam);
    	return ResultGenerator.genSuccessResult("查询用户有效的红包列表成功",userBonusDTOList);
    }

    
    //20180831 由于充值成功的时间是有些缓慢的，前端并不知道该显示多少钱的奖杯，所以去掉
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
    @ApiOperation(value="领取充值红包", notes="领取充值红包",hidden=false)
	@RequestMapping(path="/createRechargeUserBonusNew", method=RequestMethod.POST)
	public BaseResult<HashMap<String,Object>> createRechargeUserBonusNew(@RequestBody PayLogIdParam payLogIdParam) {
    	return userBonusService.createRechargeUserBonusNew(payLogIdParam);
	}
}
