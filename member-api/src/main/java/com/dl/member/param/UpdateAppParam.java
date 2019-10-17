package com.dl.member.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("app升级Param")
@Data
public class UpdateAppParam {

	@ApiModelProperty("渠道号")
	@NotNull(message = "channel不能为空")
    private String channel;	
	
	@ApiModelProperty("版本")
	@NotNull(message = "版本不能为空")
    private String version;
	
}
