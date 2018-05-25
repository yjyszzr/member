package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注册参数类
 * @author zhangzirong
 *
 */
@ApiModel("用户注册参数类")
@Data
public class UserRegisterParam {

	@NotBlank(message="手机号不能为空")
	@ApiModelProperty(value = "手机号,不能为空",required = true)
	String mobile;
	
	@NotBlank(message="密码不能为空")
	@ApiModelProperty(value = "密码,不能为空",required = true)
	String passWord;
	
	@NotBlank(message="短信验证码不能为空")
	@ApiModelProperty(value = "短信验证码,不能为空",required = true)
	String smsCode;
	
	@NotBlank(message="登录来源不能为空")
    @ApiModelProperty(value = "登录来源 1= android,2= ios,3= pc,4 =h5")
    private String loginSource;
	
    @ApiModelProperty("消息推送的唯一值")
    private String pushKey;
//	
//    @ApiModelProperty(value = "手机设备信息", required = false)
//    private UserDeviceParam device;	
}
