package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录密码参数
 *
 * @author zhangzirong
 */
@Data
public class UserLoginPassParam {
	
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号不能为空")
    private String mobileNumber;
	
    @ApiModelProperty(value = "用户登录密码")
    @NotBlank(message = "请填写用户登录密码")
    private String userLoginPass;
    
    @ApiModelProperty(value = "短信登录验证码")
    @NotBlank(message = "短信登录验证码")
    private String smsCode;

}
