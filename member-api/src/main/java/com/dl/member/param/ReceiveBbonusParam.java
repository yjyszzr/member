package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

@Data
public class ReceiveBbonusParam {

	@NotBlank(message = "手机号不能为空")
	@ApiModelProperty(value = "手机号")
	private String mobile;

	@NotBlank(message = "短信类型不能为空")
	@ApiModelProperty(value = "短信类型:0-短信登录验证码 1-注册验证码  2-忘记密码验证码")
	private String smsType;

	@NotBlank(message = "短信验证码不能为空")
	@ApiModelProperty(value = "短信验证码", required = true)
	private String smsCode;

	@NotBlank(message = "渠道Id不能为空")
	@ApiModelProperty(value = "渠道Id")
	private Integer channelId;

	@NotBlank(message = "登录来源不能为空")
	@ApiModelProperty(value = "登录来源 1= android,2= ios,3= pc,4 =h5")
	private String loginSource;
}
