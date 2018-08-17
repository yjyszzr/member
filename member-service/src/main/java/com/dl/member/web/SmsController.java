package com.dl.member.web;

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

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.SmsParam;
import com.dl.member.param.SmsParamService;
import com.dl.member.service.SmsService;
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

	/**
	 * 发送短信验证码
	 * 
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/sendSmsCode")
	public BaseResult<String> sendSms(@RequestBody SmsParam smsParam) {
		if (!RegexUtil.checkMobile(smsParam.getMobile())) {
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}

		String sameMobileKey = ProjectConstant.SMS_PREFIX + smsParam.getMobile();
		Long expireTime = stringRedisTemplate.getExpire(sameMobileKey);
		if(null != expireTime && expireTime < 54 && expireTime > 0) {
			return ResultGenerator.genResult(MemberEnums.MESSAGE_SENDLOT_ERROR.getcode(), MemberEnums.MESSAGE_SENDLOT_ERROR.getMsg());
		}
		
		int num = 0;
		String sendNumKey = "num_send_"+smsParam.getMobile();
		String sendNum3Key = sendNumKey+"_3";
		String sendNum4Key = sendNumKey+"_4";
		
		try {
			String sendNumValue = stringRedisTemplate.opsForValue().get(sendNumKey);
			if(StringUtils.isNotBlank(sendNumValue)) {
				num = Integer.parseInt(sendNumValue);
			}
		} catch (NumberFormatException e) {
			log.error("发送短信获取redis中短信发送的数量异常",e);
			return ResultGenerator.genFailResult("获取短信发送数量异常");
		}
		
		Long expireTimeLimit10 = stringRedisTemplate.getExpire(sendNum3Key);
		if(num == 3 && expireTimeLimit10 < 600 && expireTimeLimit10 > 0) {//聚合规定：10min 内不能超过3条
			return ResultGenerator.genResult(MemberEnums.MESSAGE_10MIN_COUNT_ERROR.getcode(), MemberEnums.MESSAGE_10MIN_COUNT_ERROR.getMsg());
		}
		Long expireTimeLimit60 = stringRedisTemplate.getExpire(sendNum4Key);
		if(num == 4 && expireTimeLimit60 < 3600 && expireTimeLimit60 > 0) {//聚合规定：60min 内不能超过4条
			return ResultGenerator.genResult(MemberEnums.MESSAGE_60MIN_COUNT_ERROR.getcode(), MemberEnums.MESSAGE_60MIN_COUNT_ERROR.getMsg());
		}
		if(num >= 10) {
			return ResultGenerator.genResult(MemberEnums.MESSAGE_COUNT_ERROR.getcode(), MemberEnums.MESSAGE_COUNT_ERROR.getMsg());
		}
		num++;
		
		String smsType = smsParam.getSmsType();
		String tplId = "";
		String tplValue = "";
		String strRandom4 = RandomUtil.getRandNum(4);
		User user = userService.findBy("mobile", smsParam.getMobile());
		if (ProjectConstant.VERIFY_TYPE_LOGIN.equals(smsType)) {// 登录
			if (null == user) {
				return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
			}
			tplId = memberConfig.getLOGIN_TPLID();
			strRandom4 = RandomUtil.getRandNum(4);
			tplValue = "#code#=" + strRandom4;
		} else if (ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {// 注册
			if (null != user) {
				return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
			}

			tplId = memberConfig.getREGISTER_TPLID();
			strRandom4 = RandomUtil.getRandNum(4);
			tplValue = "#code#=" + strRandom4;
		} else if (ProjectConstant.VERIFY_TYPE_FORGET.equals(smsType)) {// 忘记密码
			if (null == user) {
				return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
			}
			tplId = memberConfig.getRESETPASS_TPLID();
			strRandom4 = RandomUtil.getRandNum(4);
			tplValue = "#code#=" + strRandom4;
		}

		// 缓存验证码
		int defineExpiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
		String key = ProjectConstant.SMS_PREFIX + tplId + "_" + smsParam.getMobile();
		stringRedisTemplate.opsForValue().set(sameMobileKey, strRandom4, 60, TimeUnit.SECONDS);
		
		BaseResult<String> smsRst = smsService.sendSms(smsParam.getMobile(), tplId, tplValue);
		if (smsRst.getCode() != 0) {
			return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
		}
		
		stringRedisTemplate.opsForValue().set(key, strRandom4, defineExpiredTime, TimeUnit.SECONDS);
		int sendNumExpire = this.todayEndTime();
		stringRedisTemplate.opsForValue().set(sendNumKey, num+"", sendNumExpire, TimeUnit.SECONDS);
		stringRedisTemplate.opsForValue().set(sendNum3Key, num+"", 600, TimeUnit.SECONDS);
		stringRedisTemplate.opsForValue().set(sendNum4Key, num+"", 3600, TimeUnit.SECONDS);

		return ResultGenerator.genSuccessResult("发送短信验证码成功");
	}

	
    private int todayEndTime() {
		Calendar date = Calendar.getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minute = date.get(Calendar.MINUTE);
		int second = date.get(Calendar.SECOND);
		return (59-second) + (59-minute)*60+(23-hour)*3600;
    }
    
	/**
	 * 发送短信验证码
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
			tplId = memberConfig.getSERVICE_TPLID();
			tplValue = "#code#=" + verifyCode;
		}
		if (!TextUtils.isEmpty(tplValue)) {
			BaseResult<String> smsRst = smsService.sendSms(smsParam.getMobile(), tplId, tplValue);
			if (smsRst.getCode() != 0) {
				return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
			}
			// 缓存验证码
			int expiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
			String key = ProjectConstant.SMS_PREFIX + tplId + "_" + smsParam.getMobile();
			Long expireTime = stringRedisTemplate.getExpire(key);
			if(null != expireTime && expireTime < 54 && expireTime > 0) {
				return ResultGenerator.genResult(MemberEnums.MESSAGE_SENDLOT_ERROR.getcode(), MemberEnums.MESSAGE_SENDLOT_ERROR.getMsg());
			}
			
			stringRedisTemplate.opsForValue().set(key, verifyCode, expiredTime, TimeUnit.SECONDS);
			return ResultGenerator.genSuccessResult("发送短信验证码成功");
		} else {
			return ResultGenerator.genFailResult("参数异常");
		}
	}
}
