package com.dl.member.param;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRePwdParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "请填写手机号")
	@ApiModelProperty(value = "手机号", required = true)
	private String mobile;
	
	@NotBlank(message = "请填写密码")
	@ApiModelProperty(value = "密码", required = true)
	private String password;
	
	@NotBlank(message = "新密码")
	private String newPwd;
	
	@ApiModelProperty("登录来源 1 android，2 ios，3 pc，4 h5")
	private String loginSource;
}
