package com.dl.member.web;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.ServiceSwitchDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.param.BusiIdsListParam;
import com.dl.member.param.SysConfigParam;
import com.dl.member.service.SysConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/22.
*/
@RestController
@RequestMapping("/user/sys")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    @ApiOperation(value = "查询单个开关或阀值", notes = "查询单个开关或阀")
    @PostMapping("/querySysConfig")
    public BaseResult<SysConfigDTO> querySysConfig(@RequestBody SysConfigParam sysConfigParam) {
    	SysConfigDTO sysConfigDTO = sysConfigService.querySysConfig(sysConfigParam.getBusinessId());
    	return ResultGenerator.genSuccessResult("success", sysConfigDTO);
    }

    @ApiOperation(value = "根据业务id查询某个功能是否开启", notes = "根据业务id查询某个功能是否开启")
    @PostMapping("/queryFuncByBusiId")
    public BaseResult<ServiceSwitchDTO> queryFuncByBusiId(@RequestBody SysConfigParam sysConfigParam) {
        SysConfigDTO sysConfigDTO = sysConfigService.querySysConfig(sysConfigParam.getBusinessId());
        ServiceSwitchDTO serviceSwitchDTO = new ServiceSwitchDTO();
        serviceSwitchDTO.setBusinessId(sysConfigDTO.getBusinessId());
        serviceSwitchDTO.setTurnOn(sysConfigDTO.getValue().compareTo(BigDecimal.ZERO) == 0 ?"0":"1");
        return ResultGenerator.genSuccessResult("success", serviceSwitchDTO);
    }

    @ApiOperation(value = "查询多个开关或阀值", notes = "查询多个开关或阀值")
    @PostMapping("/querySysConfigList")
    public BaseResult<List<SysConfigDTO>> querySysConfigList(@RequestBody BusiIdsListParam busiIdsListParam) {
        List<SysConfigDTO> sysConfigDTOList = sysConfigService.querySysConfigList(busiIdsListParam.getBusinessIdList());
        return ResultGenerator.genSuccessResult("success", sysConfigDTOList);
    }

}
