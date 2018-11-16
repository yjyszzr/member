package com.dl.member.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.MediaTokenDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.param.MediaTokenParam;
import com.dl.member.service.SysConfigService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 多媒体相关
 */
@RestController
@RequestMapping("/media")
@Slf4j
public class MediaController {
	private final static Logger logger = LoggerFactory.getLogger(SmsController.class);
	
	@Resource
	private SysConfigService sysConfigService;
	
	
	/**
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "获取多媒体token", notes = "多媒体token")
	@PostMapping("/getToken")
	public BaseResult<MediaTokenDTO> getMediaToken(@RequestBody MediaTokenParam params) {
		SysConfigDTO sysDTO = null;
		switch(params.getType()) {
			case 0:
				sysDTO = sysConfigService.querySysConfig(18);			
				break;
			case 1:
				sysDTO = sysConfigService.querySysConfig(19);
				break;
			case 2:
				sysDTO = sysConfigService.querySysConfig(20);
				break;
		}
		if(sysDTO == null) {
			return ResultGenerator.genFailResult();
		}
		String valueTxt = sysDTO.getValueTxt();
		MediaTokenDTO tokenEntity = JSON.parseObject(valueTxt,MediaTokenDTO.class);
		String fileName = MediaTokenDTO.getFileName();
		tokenEntity.setFileName(fileName);
		return ResultGenerator.genSuccessResult("succ",tokenEntity);
	}
}
