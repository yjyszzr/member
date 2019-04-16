package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户登录信息
 *
 * @author zhangzirong
 */
@ApiModel("用户登录信息")
@Data
public class UserLoginForXNDTO implements Serializable {

	private static final long serialVersionUID = 3986112934396561329L;
	@ApiModelProperty("token")
	private String token;
	@ApiModelProperty("用户手机号")
	private String mobile;
	@ApiModelProperty("用户昵称")
	private String nickName;
	@ApiModelProperty("用户头像地址")
	private String headImg;
	@ApiModelProperty("是否进行了实名认证:0-未认证  1-已认证")
	private String isReal;
	@ApiModelProperty("是否是白名单:0或者空不是 1-是")
	private String isWhite;

}
