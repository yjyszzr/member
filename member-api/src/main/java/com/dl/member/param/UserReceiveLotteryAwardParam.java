package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

@ApiModel("领取彩金接口")
@Data
public class UserReceiveLotteryAwardParam {

	@NotBlank(message = "请填写手机号")
	@ApiModelProperty(value = "手机号", required = true)
	private String mobile;

	@NotBlank(message = "短信验证码不能为空")
	@ApiModelProperty(value = "短信验证码", required = true)
	private String smsCode;

	@ApiModelProperty(value = "渠道分销员Id")
	private Integer channelDistributorId;

	@ApiModelProperty("登录来源  1 android，2 ios，3 pc，4 h5")
	private String loginSource;
}
