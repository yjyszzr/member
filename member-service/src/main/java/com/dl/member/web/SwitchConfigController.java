package com.dl.member.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.param.IDFACallBackParam;
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
    public BaseResult<SwitchConfigDTO> add(@RequestBody StrParam strparam) {
    	UserDeviceInfo userDevice = SessionUtil.getUserDevice();
    	String inPrams = JSON.toJSONString(userDevice);
    	String logId = DateUtil.getCurrentDateTime();
    	log.info(logId + "====================================版本参数:"+inPrams);
    	Integer userSwitchByIp = switchConfigService.userSwitchByIp();
    	log.info(logId + "===========判断用户ip所属区域是否打开交易返回：" + userSwitchByIp);
    	if(userSwitchByIp.equals(ProjectConstant.BISINESS_APP_CLOSE)) {
    		SwitchConfigDTO switchConfig = new SwitchConfigDTO();
    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
    		log.info(logId + "====非国内IP或别的区域=======判断用户ip为非需要打开交易,现执行关闭交易版返回");
    		return ResultGenerator.genSuccessResult("success",switchConfig);
    	}
    	String plat = "";
    	if(userDevice.getPlat().equals("android")) {
    		plat = "1";
    	}else if(userDevice.getPlat().equals("iphone")) {
    		plat = "0";
    		//idfa 回调、存储  （lidelin）
    		IDFACallBackParam idfaParam = new IDFACallBackParam();
    		idfaParam.setUserid(-1);
    		idfaParam.setIdfa(userDevice.getIDFA());
    		iDFAService.callBackIdfa(idfaParam);
    	}else if(userDevice.getPlat().equals("h5")) {
    		plat = "2";
    	}else {
    		return ResultGenerator.genFailResult("设备信息中的plat参数错误");
    	}
    	
    	return switchConfigService.querySwitch(plat, userDevice.getAppv(),userDevice.getChannel());
    }

}
