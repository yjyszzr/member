package com.dl.member.web;
import com.alibaba.fastjson.JSON;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.model.SwitchConfig;
import com.dl.member.param.DeviceKeyParam;
import com.dl.member.param.DeviceParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.SwitchConfigParam;
import com.dl.member.service.SwitchConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class SwitchConfigController {
    @Resource
    private SwitchConfigService switchConfigService;

    @ApiOperation(value = "根据平台和业务版本查询当前版本是否开启", notes = "根据平台和业务版本查询当前版本是否开启")
    @PostMapping("/query")
    public BaseResult<SwitchConfig> add(@RequestBody StrParam strparam) {
    	UserDeviceInfo userDevice = SessionUtil.getUserDevice();
    	String inPrams = JSON.toJSONString(userDevice);
    	log.info(DateUtil.getCurrentDateTime()+"====================================版本参数:"+inPrams);
    	String plat = "";
    	if(userDevice.getPlat().equals("android")) {
    		plat = "1";
    	}else if(userDevice.getPlat().equals("iphone")) {
    		plat = "0";
    	}else if(userDevice.getPlat().equals("h5")) {
    		plat = "2";
    	}else {
    		return ResultGenerator.genFailResult("设备信息中的plat参数错误");
    	}
    	
    	return switchConfigService.querySwitchConfig(plat, userDevice.getAppv());
    }

}
