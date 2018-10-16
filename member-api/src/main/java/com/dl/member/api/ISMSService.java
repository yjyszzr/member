package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.param.SmsParam;

/**
 * 短信发送接口
 * 
 * @author Mr.Li
 *
 */
@FeignClient(value = "member-service")
public interface ISMSService {

	@RequestMapping(path = "/sms/sendSmsCode", method = RequestMethod.POST)
	public BaseResult<String> sendSmsCode(@RequestBody SmsParam smsParam);

	@RequestMapping(path = "/sms/getRedisSmsCode", method = RequestMethod.POST)
	public String getRedisSmsCode(@RequestBody String mobile);

	@RequestMapping(path = "/sms/deleteRedisSmsCode", method = RequestMethod.POST)
	public void deleteRedisSmsCode(@RequestBody String mobile);
}
