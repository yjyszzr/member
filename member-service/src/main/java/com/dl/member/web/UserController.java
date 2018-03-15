package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.MobileNumberParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserLoginPassParam;
import com.dl.member.service.UserService;
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
    	UserDTO userDTO = userService.queryUserByUserIdExceptPass();
    	return ResultGenerator.genSuccessResult("查询用户信息成功", userDTO);
    }
    	  
}
