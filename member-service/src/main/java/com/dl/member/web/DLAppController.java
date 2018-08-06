package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.UpdateAppDTO;
import com.dl.member.model.DLApp;
import com.dl.member.param.UpdateAppParam;
import com.dl.member.service.DLAppService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

/**
* Created by CodeGenerator on 2018/08/06.
*/
@RestController
@RequestMapping("/user/app")
public class DLAppController {
    @Resource
    private DLAppService dLAppService;

    @ApiOperation(value = "升级app", notes = "升级app")
    @PostMapping("/updateApp")
    public BaseResult<UpdateAppDTO> updateApp(@RequestBody @Valid UpdateAppParam updateAppParam) {
    	return dLAppService.queryUpdateApp(updateAppParam.getChannel(), updateAppParam.getVersion());
    }
}
