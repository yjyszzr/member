package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

@Data
public class SmsParamService {
	@ApiModelProperty(value = "手机号")
	@NotBlank(message = "手机号")
	private String mobile;

	@ApiModelProperty(value = "短信类型:0-短信登录验证码 1-注册验证码  2-忘记密码验证码")
	@NotBlank(message = "短信类型不能为空")
	private String smsType;

	@ApiModelProperty(value = "验证码")
	@NotBlank(message = "验证码")
	private String verifyCode;

	@ApiModelProperty(value = "是否需要校验注册")
	@NotBlank(message = "是否需要校验注册 0不需要,1需要")
	private int needVerifyReg;
}
