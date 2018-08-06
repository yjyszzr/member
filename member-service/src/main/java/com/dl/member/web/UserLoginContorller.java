package com.dl.member.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserLoginWithPassParam;
import com.dl.member.param.UserLoginWithSmsParam;
import com.dl.member.service.UserLoginService;
import com.dl.member.util.TokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户登录接口
 * zhangzirong
 */
@Api(value = "用户登录接口")
@RestController
@RequestMapping("/login")
@Slf4j
public class UserLoginContorller {
    @Resource
    private UserLoginService userLoginService;

    @ApiOperation(value = "密码登录", notes = "密码登录")
    @PostMapping("/loginByPass")
    public BaseResult<UserLoginDTO> loginByPass( @RequestBody UserLoginWithPassParam userLoginMobileParam, HttpServletRequest request) {
    	BaseResult<UserLoginDTO> loginByPass = userLoginService.loginByPass(userLoginMobileParam);
    	if(loginByPass.getCode() == 0) {
    		String token = loginByPass.getData().getToken();
    		request.getSession().setAttribute("user_token", token);;
    	}
    	return loginByPass;
    }
    
    @ApiOperation(value = "短信验证码登录", notes = "短信验证码登录")
    @PostMapping("/loginBySms")
    public BaseResult<UserLoginDTO> loginBySms(@RequestBody UserLoginWithSmsParam userLoginMobileParam, HttpServletRequest request) {
    	BaseResult<UserLoginDTO> loginBySms = userLoginService.loginBySms(userLoginMobileParam, request);
    	if(loginBySms.getCode() == 0) {
    		String token = loginBySms.getData().getToken();
    		request.getSession().setAttribute("user_token", token);;
    	}
    	return loginBySms;
    }
    
    @ApiOperation(value = "用户注销", notes = "用户注销")
    @PostMapping("/logout")
    public BaseResult<String> logout(@RequestBody StrParam strPaaram, HttpServletRequest request) {
    	userLoginService.loginLogOut();
        TokenUtil.invalidateCurToken();
        request.getSession().removeAttribute("user_token");
        return ResultGenerator.genSuccessResult("用户注销成功");
    }
    
}
