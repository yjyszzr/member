package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.UserRegisterParam;
import com.dl.member.service.UserBonusService;
import com.dl.member.service.UserLoginService;
import com.dl.member.service.UserRegisterService;
import com.dl.member.service.UserService;
import com.dl.member.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* Created by CodeGenerator on 2018/03/08.
*/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserRegisterController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
	
    @Resource
    private UserRegisterService userRegisterService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private UserLoginService userLoginService;
    
    @Resource
    private UserBonusService userBonusService;

    /**
     * 新用户注册:
     * @param userRegisterParam
     * @param request
     * @return
     */
    @ApiOperation(value = "新用户注册", notes = "新用户注册")
    @PostMapping("/register")
    public BaseResult<UserLoginDTO> register(@RequestBody UserRegisterParam userRegisterParam, HttpServletRequest request) {
        String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.REGISTER_TPLID + "_" + userRegisterParam.getMobile());
        if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userRegisterParam.getSmsCode())) {
            return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
        }
    	
    	BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
    	if(regRst.getCode() != 0) {
    		return ResultGenerator.genResult(regRst.getCode(),regRst.getMsg());
    	}
    	
    	userBonusService.receiveUserBonus(ProjectConstant.REGISTER,regRst.getData());
    	
    	Integer userId = regRst.getData();
    	TokenUtil.genToken(userId, Integer.valueOf(userRegisterParam.getLoginSource()));
    	UserLoginDTO userLoginDTO = userLoginService.queryUserLoginDTOByMobile(userRegisterParam.getMobile(), userRegisterParam.getLoginSource());
		
		return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
    }
    
    
    
}
