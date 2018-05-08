package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.UserDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.MobileNumberParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserLoginPassParam;
import com.dl.member.service.UserService;
import com.dl.member.util.TokenUtil;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
* Created by CodeGenerator on 2018/03/08.
*/
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    

    /**
     * 校验手机号
     * @param mobileNumberParam
     * @return
     */
    @ApiOperation(value = "校验手机号", notes = "校验手机号")
    @PostMapping("/validateMobile")
    public BaseResult<String> validateMobile(@RequestBody MobileNumberParam mobileNumberParam) {  
    	return userService.validateUserMobile(mobileNumberParam.getMobileNumber());
    }
    
    /**
     * 修改用户登录密码
     * @param userLoginPassParam
     * @return
     */
    @ApiOperation(value = "修改用户登录密码", notes = "修改用户登录密码")
    @PostMapping("/updateLoginPass")
    public BaseResult<String> updateLoginPass(@RequestBody UserLoginPassParam userLoginPassParam){
    	return userService.updateUserLoginPass(userLoginPassParam.getUserLoginPass(),userLoginPassParam.getMobileNumber(),userLoginPassParam.getSmsCode());
    }
    
    
    /**
     * 查询用户信息除了登录密码和支付密码
     * @param userLoginPassParam
     * @return
     */
    @ApiOperation(value = "查询用户信息除了登录密码和支付密码", notes = "查询用户信息除了登录密码和支付密码")
    @PostMapping("/userInfoExceptPass")
    public BaseResult<UserDTO> queryUserInfo(@RequestBody StrParam strParam){
    	return userService.queryUserByUserIdExceptPass();
    }
    
    /**
     * 根据手机号获取token
     * @param mobileNumberParam
     * @return
     */
//    @ApiOperation(value = "根据手机号获取token(不提供调用)", notes = "根据手机号获取token(不提供调用)")
//    @PostMapping("/getTokenByMobile")
//    public BaseResult<String> getTokenByMobile(@RequestBody MobileNumberParam mobileNumberParam) {
//    	User user = userService.findBy("mobile", mobileNumberParam.getMobileNumber());
//    	if(null == user) {
//    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "没有该用户，无法生成token");
//    	}
//    	
//    	String token = TokenUtil.genToken(user.getUserId(), 1);
//    	return ResultGenerator.genSuccessResult("获取token成功", token);
//    }
    	  
}
