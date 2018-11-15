package com.dl.member.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.MediaTokenDTO;
import com.dl.member.param.MediaTokenParam;
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
	
	/**
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "获取多媒体token", notes = "多媒体token")
	@PostMapping("/getToken")
	public BaseResult<MediaTokenDTO> getMediaToken(@RequestBody MediaTokenParam strparam) {
		MediaTokenDTO tokenEntity = MediaTokenDTO.getEntity(strparam.getType());
		return ResultGenerator.genSuccessResult("succ",tokenEntity);
	}
}
