package com.dl.member.web;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.RandomUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.SmsParam;
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
    	String smsType = smsParam.getSmsType();
    	String tplId = "";
    	String tplValue = "";
    	String strRandom4 = RandomUtil.getRandNum(4);
    	if("0".equals(smsType)) {//登录
    		
    		tplId = ProjectConstant.LOGIN_TPLID;
    		strRandom4 = RandomUtil.getRandNum(4);
    		tplValue = "#code#="+strRandom4;
    	}else if("1".equals(smsType)) {//注册
        	User user = userService.findBy("mobile", smsParam.getMobile());
        	if(null != user) {
       		 	return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
        	}
    		
    		tplId = ProjectConstant.REGISTER_TPLID;
    		strRandom4 = RandomUtil.getRandNum(4);
    		tplValue = "#code#="+strRandom4;
    	}else if("2".equals(smsType)) {//忘记密码
        	User user = userService.findBy("mobile", smsParam.getMobile());
        	if(null == user) {
       		 	return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
        	}
        	
    		tplId = ProjectConstant.RESETPASS_TPLID;
    		strRandom4 = RandomUtil.getRandNum(4);
    		tplValue = "#code#="+strRandom4;
    	}else {
    		
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
}
