package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.model.UserBonus;
import com.dl.member.param.RollackSurplusPayParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserBonusIdParam;
import com.dl.member.param.UserBonusStatusParam;
import com.dl.member.service.UserBonusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
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
    
    @ApiOperation(value="根据状态查询有效的红包集合", notes="根据状态查询有效的红包集合",hidden=false)
    @PostMapping("/queryBonusListByStatus")
    public BaseResult<PageInfo<UserBonusDTO>> queryBonusListByStatus(@RequestBody UserBonusStatusParam userBonusStatusParam) {
    	PageInfo<UserBonusDTO> userBonusDTOList =  userBonusService.queryBonusListByStatus(userBonusStatusParam.getStatus(),userBonusStatusParam.getPageNum(),userBonusStatusParam.getPageSize());
    	return ResultGenerator.genSuccessResult("根据状态查询有效的红包集合成功",userBonusDTOList);
    }
    
    @ApiOperation(value="查询用户有效的红包列表", notes="查询用户有效的红包列表",hidden=false)
    @PostMapping("/queryValidBonusList")
    public BaseResult<List<UserBonusDTO>> queryValidBonusList(@RequestBody StrParam strParam) {
    	List<UserBonusDTO> userBonusDTOList =  userBonusService.queryValidBonusList();
    	return ResultGenerator.genSuccessResult("查询用户有效的红包列表成功",userBonusDTOList);
    }
    
}
