package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.ClickNumParam;
import com.dl.member.param.ReceiveBbonusParam;
import com.dl.member.param.UserRegisterParam;
import com.dl.member.param.XqdSmsParam;
import com.dl.member.service.DlChannelConsumerService;
import com.dl.member.service.DlChannelDistributorService;
import com.dl.member.service.DlXqdService;
import com.dl.member.service.SmsService;
import com.dl.member.service.UserService;

/**
 * 
 * @author Mr.Li 向钱贷活动模块
 *
 */
@RestController
@RequestMapping("/xqdActivity")
public class DlXqdController {
	@Resource
	private DlChannelConsumerService dlChannelConsumerService;
	@Resource
	private DlChannelDistributorService dlChannelDistributorService;
	@Resource
	private UserService userService;
	@Resource
	private DlXqdService xqdService;
	@Resource
	private MemberConfig memberConfig;
	@Resource
	private SmsService smsService;
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/smsCode")
	public BaseResult<String> sendServiceSms(@RequestBody XqdSmsParam smsParam, HttpServletRequest request) {
		String smsType = smsParam.getSmsType();
		String tplId = "";
		String tplValue = "";
		String strRandom4 = RandomUtil.getRandNum(4);
		if (ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {
			User user = userService.findBy("mobile", smsParam.getMobile());
			if (user != null) {
				return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
			}

			if (!RegexUtil.checkMobile(smsParam.getMobile())) {
				return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
			}

			tplId = memberConfig.getREGISTER_TPLID();
			tplValue = "#code#=" + strRandom4;
		}
		if (!TextUtils.isEmpty(tplValue)) {
			BaseResult<String> smsRst = smsService.sendSms(smsParam.getMobile(), tplId, tplValue);
			if (smsRst.getCode() != 0) {
				return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
			}
			// 缓存验证码
			int expiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
			String key = ProjectConstant.SMS_PREFIX + tplId + "_" + smsParam.getMobile();
			// 验证码写入缓存
			stringRedisTemplate.opsForValue().set(key, strRandom4, expiredTime, TimeUnit.SECONDS);
			// 记录点击量
			stringRedisTemplate.opsForHash().increment("channelSms:" + smsParam.getChannelId(), DateUtil.getCurrentDate(DateUtil.yyyyMMdd), 1);
			return ResultGenerator.genSuccessResult("发送短信验证码成功", smsParam.getMobile());
		} else {
			return ResultGenerator.genSuccessResult("短信验证码存储失败", smsParam.getMobile());
		}
	}

	@ApiOperation(value = "领取新人红包", notes = "领取新人红包")
	@PostMapping("/receiveRonus")
	public BaseResult<String> receiveRonus(@RequestBody ReceiveBbonusParam rParam, HttpServletRequest request) {
		String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.REGISTER_TPLID + "_" + rParam.getMobile());
		if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(rParam.getSmsCode())) {
			return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
		}
		// 记录领取人数保存到redis
		stringRedisTemplate.opsForHash().increment("channelReceiveRonusNum:" + rParam.getChannelId(), DateUtil.getCurrentDate(DateUtil.yyyyMMdd), 1);
		// 短信发送成功执行店员保存操作
		UserRegisterParam userRegisterParam = new UserRegisterParam();
		userRegisterParam.setLoginSource(rParam.getLoginSource());
		userRegisterParam.setMobile(rParam.getMobile());
		userRegisterParam.setPassWord("");
		User user = userService.findBy("mobile", userRegisterParam.getMobile());
		if (null != user) {
			return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), "该手机号已经注册");
		}
		return xqdService.registerUser(userRegisterParam, request);
	}

	@ApiOperation(value = "点击数量接口", notes = "通用的点击类型,clickTypeId要求全局唯一")
	@PostMapping("/clickNum")
	public BaseResult<String> clickNum(@RequestBody ClickNumParam param) {
		// 记录点击量到redis
		stringRedisTemplate.opsForHash().increment("channelClickNum:" + param.getClickTypeId(), DateUtil.getCurrentDate(DateUtil.yyyyMMdd), 1);
		return ResultGenerator.genSuccessResult();
	}
}
