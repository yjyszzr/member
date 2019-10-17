package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class DlChannelDistributorParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "用户id")
	private Integer userId;
}
