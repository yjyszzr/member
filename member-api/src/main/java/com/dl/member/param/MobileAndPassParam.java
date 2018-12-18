package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MobileAndPassParam {
	
	@ApiModelProperty(value="手机号")
	private String mobile;

	@ApiModelProperty(value="密码")
	private String pass;

}
