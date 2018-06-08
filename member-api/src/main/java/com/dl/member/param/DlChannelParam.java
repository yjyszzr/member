package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class DlChannelParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "渠道名称")
	private String ChannelName;
}
