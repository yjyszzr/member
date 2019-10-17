package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MobileInfoParam {
	@ApiModelProperty(value = "电话")
	String mobile;

}
