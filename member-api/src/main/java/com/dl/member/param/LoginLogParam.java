package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录日志
 * 
 * @author Mr.Li
 *
 */
@Data
public class LoginLogParam {

	@ApiModelProperty(value = "用户Id")
	private Integer userId;

	@ApiModelProperty(value = "登录类型")
	private Integer loginType;

	@ApiModelProperty(value = "登录状态")
	private int loginSstatus;

	@ApiModelProperty(value = "登录参数")
	private String loginParams;

	@ApiModelProperty(value = "登录结果")
	private String loginResult;

}
