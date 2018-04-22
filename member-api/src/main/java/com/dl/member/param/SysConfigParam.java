package com.dl.member.param;


import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("业务配置信息")
@Data
public class SysConfigParam {

	@ApiModelProperty("业务id")
	@NotNull
    private Integer businessId;
	
}