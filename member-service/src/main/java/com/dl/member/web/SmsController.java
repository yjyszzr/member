package com.dl.member.web;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
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
    
    /**
     * 发送短信验证码
     * @param mobileNumberParam
     * @return
     */
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @PostMapping("/sendSmsCode")
    public BaseResult<String> sendSms(@RequestBody SmsParam smsParam){
    	if(!RegexUtil.checkMobile(smsParam.getMobile())) {
    		return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
    	}
    	String smsType = smsParam.getSmsType();
    	String tplId = "";
    	String tplValue = "";
    	String strRandom4 = RandomUtil.getRandNum(4);
    	User user = userService.findBy("mobile", smsParam.getMobile());
    	if(ProjectConstant.VERIFY_TYPE_LOGIN.equals(smsType)) {//登录
        	if(null == user) {
       		 	return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
        	}
    		tplId = ProjectConstant.LOGIN_TPLID;
    		strRandom4 = RandomUtil.getRandNum(4);
    		tplValue = "#code#="+strRandom4;
    	}else if(ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {//注册
        	if(null != user) {
       		 	return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
        	}
    		
    		tplId = ProjectConstant.REGISTER_TPLID;
    		strRandom4 = RandomUtil.getRandNum(4);
    		tplValue = "#code#="+strRandom4;
    	}else if(ProjectConstant.VERIFY_TYPE_FORGET.equals(smsType)) {//忘记密码
        	if(null == user) {
       		 	return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
        	}
    		tplId = ProjectConstant.RESETPASS_TPLID;
    		strRandom4 = RandomUtil.getRandNum(4);
    		tplValue = "#code#="+strRandom4;
    	}
    	
    	BaseResult<String> smsRst = smsService.sendSms(smsParam.getMobile(), tplId, tplValue);
    	if(smsRst.getCode() != 0) {
    		return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
    	}
    	
    	//缓存验证码
    	int expiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
    	String key = ProjectConstant.SMS_PREFIX + tplId + "_" + smsParam.getMobile();
    	stringRedisTemplate.opsForValue().set(key, strRandom4, expiredTime, TimeUnit.SECONDS);
    	
    	return ResultGenerator.genSuccessResult("发送短信验证码成功");
    }
    
    
    /**
     * 发送短信验证码
     * @param mobileNumberParam
     * @return
     */
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @PostMapping("/sendServiceSmsCode")
    public BaseResult<String> sendServiceSms(@RequestBody SmsParamService smsParam){
    	String smsType = smsParam.getSmsType();
    	String tplId = "";
    	String tplValue = "";
    	String verifyCode = smsParam.getVerifyCode();
    	if(ProjectConstant.VERIFY_TYPE_SERVICE.equals(smsType)){
    		User user = userService.findBy("mobile", smsParam.getMobile());
    		if(user == null) {
    			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
    		}
    		tplId = ProjectConstant.SERVICE_TPLID;
    		tplValue = "#code#="+verifyCode;
    	}
    	if(!TextUtils.isEmpty(tplValue)) {
    		BaseResult<String> smsRst = smsService.sendSms(smsParam.getMobile(), tplId, tplValue);
    		if(smsRst.getCode() != 0) {
        		return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
        	}
    		//缓存验证码
        	int expiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
        	String key = ProjectConstant.SMS_PREFIX + tplId + "_" + smsParam.getMobile();
        	stringRedisTemplate.opsForValue().set(key, verifyCode, expiredTime, TimeUnit.SECONDS);
        	return ResultGenerator.genSuccessResult("发送短信验证码成功");	
    	}else {
    		return ResultGenerator.genFailResult("参数异常");
    	}
    }
}
