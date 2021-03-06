package com.dl.member.web;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.SmsParam;
import com.dl.member.param.SmsParamService;
import com.dl.member.service.DlPhoneChannelService;
import com.dl.member.service.SmsService;
import com.dl.member.service.SmsTemplateService;
import com.dl.member.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {

	private final static Logger logger = LoggerFactory.getLogger(SmsController.class);

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

			tplId = "99569";// 都用的是彩小秘的签名
			tplValue = "#code#=" + verifyCode + "&#m#=" + 5;
		}

		if (!TextUtils.isEmpty(tplValue)) {
			BaseResult<String> smsRst = smsService.sendJuheSms(smsParam.getMobile(), tplId, tplValue);
			if (smsRst.getCode() != 0) {
			    log.error(smsRst.getData());
				return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
			}
			// 缓存验证码
			String key = ProjectConstant.SMS_PREFIX + smsType + "_" + smsParam.getMobile();
			Long expireTime = stringRedisTemplate.getExpire(key);
			if (null != expireTime && expireTime < 59 && expireTime > 0) {
				return ResultGenerator.genResult(MemberEnums.MESSAGE_SENDLOT_ERROR.getcode(), MemberEnums.MESSAGE_SENDLOT_ERROR.getMsg());
			}

			stringRedisTemplate.opsForValue().set(key, verifyCode, 300, TimeUnit.SECONDS);
			return ResultGenerator.genSuccessResult("发送短信验证码成功");
		} else {
			return ResultGenerator.genFailResult("参数异常");
		}
	}

	@ApiOperation(value = "获取redis里的验证码", notes = "获取redis里的验证码")
	@PostMapping("/getRedisSmsCode")
	public String getRedisSmsCode(@RequestBody String mobile) {
		logger.info("获取redis里的验证码所需的key:==============={}", ProjectConstant.SMS_PREFIX + "0_" + mobile);
		String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + "0_" + mobile);
		logger.info("redis里的验证码:==============={}", cacheSmsCode);
		return cacheSmsCode;
	}

	@ApiOperation(value = "删除redis里的短信验证码", notes = "删除redis里的短信验证码")
	@PostMapping("/deleteRedisSmsCode")
	public void deleteRedisSmsCode(@RequestBody String mobile) {
		stringRedisTemplate.delete(ProjectConstant.SMS_PREFIX + "0_" + mobile);
	}
}
