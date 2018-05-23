package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MyQRCodeParam {
	@ApiModelProperty("店员Id")
	private Integer userId;
}
