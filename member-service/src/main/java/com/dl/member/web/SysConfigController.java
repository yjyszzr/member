package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.model.SwitchConfig;
import com.dl.member.model.SysConfig;
import com.dl.member.param.SwitchConfigParam;
import com.dl.member.param.SysConfigParam;
import com.dl.member.service.SysConfigService;
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
* Created by CodeGenerator on 2018/04/22.
*/
@RestController
@RequestMapping("/user/sys")
public class SysConfigController {
    @Resource
    private SysConfigService sysConfigService;

    
    @ApiOperation(value = "根据平台和业务版本查询当前版本是否开启", notes = "根据平台和业务版本查询当前版本是否开启")
    @PostMapping("/querySysConfig")
    public BaseResult<SysConfigDTO> querySysConfig(@RequestBody SysConfigParam sysConfigParam) {
    	SysConfigDTO sysConfigDTO = sysConfigService.querySysConfig(sysConfigParam.getBusinessId());
    	return ResultGenerator.genSuccessResult("success", sysConfigDTO);
    }
}
