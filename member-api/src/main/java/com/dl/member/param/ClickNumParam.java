package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClickNumParam {

	@ApiModelProperty(value = "点击类型Id")
	private String clickTypeId;

}
