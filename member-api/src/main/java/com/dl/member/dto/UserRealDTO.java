package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 真实用户信息
 * @author zhangzirong
 *
 */
@ApiModel("真实用户信息")
@Data
public class UserRealDTO {
	@ApiModelProperty("真实姓名")
    private String realName;
	@ApiModelProperty("身份证号")
	private String iDCode;
	@ApiModelProperty("cardPic1")
	private String cardPic1;	
	@ApiModelProperty("cardPic2")
	private String cardPic2;
	@ApiModelProperty("cardPic3")
	private String cardPic3;
	@ApiModelProperty("status:0 -非当前默认，1 -当前默认")
	private String status;	
}
