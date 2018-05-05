package com.dl.member.web;
import java.time.Instant;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.DlComplain;
import com.dl.member.param.DlComplainAddParam;
import com.dl.member.service.DlComplainService;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/05/05.
*/
@RestController
@RequestMapping("/dl/complain")
public class DlComplainController {
    @Resource
    private DlComplainService dlComplainService;

    @ApiOperation(value = "用户投诉接口", notes = "用户投诉接口")
    @PostMapping("/add")
    public BaseResult add(@RequestBody DlComplainAddParam param) {
    	String complainContent = param.getComplainContent();
    	if(StringUtils.isBlank(complainContent)) {
    		return ResultGenerator.genResult(MemberEnums.COMPLAIN_CONTENT_NULL.getcode(), MemberEnums.COMPLAIN_CONTENT_NULL.getMsg());
    	}
    	DlComplain dlComplain = new DlComplain();
    	dlComplain.setComplainContent(complainContent);
    	dlComplain.setComplainer(SessionUtil.getUserId());
    	Long complainTime = Instant.now().getEpochSecond();
    	dlComplain.setComplainTime(complainTime.intValue());
    	dlComplain.setIsRead(0);
        dlComplainService.save(dlComplain);
        return ResultGenerator.genSuccessResult();
    }

    /*@PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlComplainService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlComplain dlComplain) {
        dlComplainService.update(dlComplain);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlComplain dlComplain = dlComplainService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlComplain);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlComplain> list = dlComplainService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }*/
}
