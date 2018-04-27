package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.member.model.SwitchConfig;
import com.dl.member.param.DeviceParam;
import com.dl.member.param.SwitchConfigParam;
import com.dl.member.service.SwitchConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/16.
*/
@RestController
@RequestMapping("/switch/config")
public class SwitchConfigController {
    @Resource
    private SwitchConfigService switchConfigService;

//    @ApiOperation(value = "根据平台和业务版本查询当前版本是否开启", notes = "根据平台和业务版本查询当前版本是否开启")
//    @PostMapping("/query")
//    public BaseResult<List<SwitchConfig>> add(@RequestBody DeviceParam deviceParam) {
//    	return switchConfigService.querySwitchConfig(deviceParam.getPlat(), deviceParam.getVersion());
//    }

}
