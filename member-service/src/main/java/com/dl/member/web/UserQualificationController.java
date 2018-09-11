package com.dl.member.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.dto.QFDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.QFParam;
import com.dl.member.service.UserQualificationService;

import io.swagger.annotations.ApiOperation;

/**
* Created by zhangzirong on 2018/08/22.
*/
@RestController
@RequestMapping("/user/qualification")
public class UserQualificationController {
    @Resource
    private UserQualificationService userQualificationService;

    @ApiOperation(value = "领取活动资格", notes = "领取活动资格")
    @PostMapping("/reaceiveActQF")
    public BaseResult<String> reaceiveActQF(@RequestBody QFParam qfParam) {
    	BaseResult<QFDTO> qfDtoRst = this.queryActQF(qfParam);
    	int qfRst = qfDtoRst.getData().getQfRst();
    	if(1 == qfRst) {
    		return ResultGenerator.genResult(MemberEnums.DATA_ALREADY_EXIT_IN_DB.getcode(), "您已有该活动资格,无需领取");
    	}
    	int rst = userQualificationService.reaceiveActQF(Integer.valueOf(qfParam.getAct_id()), Integer.valueOf(qfParam.getAct_type()));
        if(rst != 1) {
        	return ResultGenerator.genFailResult("领取失败");
        }
        return ResultGenerator.genSuccessResult("领取成功");
    }

    @ApiOperation(value = "查询活动资格", notes = "查询活动资格")
    @PostMapping("/queryActQF")
    public BaseResult<QFDTO> queryActQF(@RequestBody QFParam qfParam) {
    	Integer userId = SessionUtil.getUserId();
    	if(null == userId) {
    		return ResultGenerator.genNeedLoginResult("请先登录");
    	}
     	int qfRst = userQualificationService.queryActQF(Integer.valueOf(qfParam.getAct_id()), Integer.valueOf(qfParam.getAct_type()));
     	QFDTO qfDto = new QFDTO();
     	qfDto.setQfRst(qfRst);
     	if(1 != qfRst) {
     		return ResultGenerator.genSuccessResult("您暂无活动资格,请先领取",qfDto);
     	}
     	return ResultGenerator.genSuccessResult("有活动资格",qfDto);
    }
    
    
}
