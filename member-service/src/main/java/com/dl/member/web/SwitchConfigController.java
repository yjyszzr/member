package com.dl.member.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.param.StrParam;
import com.dl.member.service.IDFAService;
import com.dl.member.service.SwitchConfigService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
* Created by CodeGenerator on 2018/04/16.
*/
@RestController
@RequestMapping("/switch/config")
@Slf4j
public class SwitchConfigController {
    @Resource
    private SwitchConfigService switchConfigService;
    @Resource
	private IDFAService iDFAService;
    
    @ApiOperation(value = "根据平台和业务版本查询当前版本是否开启", notes = "根据平台和业务版本查询当前版本是否开启")
    @PostMapping("/query")
    public BaseResult<SwitchConfigDTO> querySwitch(@RequestBody StrParam param) {
    	return switchConfigService.querySwitch();
    }

}
