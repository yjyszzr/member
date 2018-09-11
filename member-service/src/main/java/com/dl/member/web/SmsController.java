package com.dl.member.web;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.SmsTemplate;
import com.dl.member.model.User;
import com.dl.member.param.SmsParam;
import com.dl.member.param.SmsParamService;
import com.dl.member.service.DlPhoneChannelService;
import com.dl.member.service.SmsService;
import com.dl.member.service.SmsTemplateService;
import com.dl.member.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private SmsService smsService;

	@Resource
	private UserService userService;
	
	@Resource
	private MemberConfig memberConfig;
	
	@Resource
	private SmsTemplateService smsTemplateService;
	
	@Resource
	private DlPhoneChannelService dlPhoneChannelService;

	/**
	 * 发送短信验证码
	 * 
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/sendSmsCode")
	public BaseResult<String> sendSms(@RequestBody SmsParam smsParam) {
		return smsService.sendSms(smsParam);
	}

	/**
	 * 发送短信验证码,单独给后台管理用
	 * 
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/sendServiceSmsCode")
	public BaseResult<String> sendServiceSms(@RequestBody SmsParamService smsParam) {
		String smsType = smsParam.getSmsType();
		String tplId = "";
		String tplValue = "";
		String verifyCode = smsParam.getVerifyCode();
		int needVerifyReg = smsParam.getNeedVerifyReg();
		if (ProjectConstant.VERIFY_TYPE_SERVICE.equals(smsType)) {
			if (needVerifyReg == 1) {
				User user = userService.findBy("mobile", smsParam.getMobile());
				if (user == null) {
					return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
				}
			}
			
			tplId = "99569";//都用的是彩小秘的签名
			tplValue = "#code#=" + verifyCode +"&#m#=" + 5;
		}
		
		if (!TextUtils.isEmpty(tplValue)) {
			BaseResult<String> smsRst = smsService.sendJuheSms(smsParam.getMobile(), tplId, tplValue);
			if (smsRst.getCode() != 0) {
				return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
			}
			// 缓存验证码
			String key = ProjectConstant.SMS_PREFIX + smsType + "_" + smsParam.getMobile();
			Long expireTime = stringRedisTemplate.getExpire(key);
			if(null != expireTime && expireTime < 59 && expireTime > 0) {
				return ResultGenerator.genResult(MemberEnums.MESSAGE_SENDLOT_ERROR.getcode(), MemberEnums.MESSAGE_SENDLOT_ERROR.getMsg());
			}
			
			stringRedisTemplate.opsForValue().set(key, verifyCode, 300, TimeUnit.SECONDS);
			return ResultGenerator.genSuccessResult("发送短信验证码成功");
		} else {
			return ResultGenerator.genFailResult("参数异常");
		}
	}
}
