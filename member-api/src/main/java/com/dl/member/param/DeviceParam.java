package com.dl.member.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceParam {
	 @ApiModelProperty(value = "手机品牌")
	 private String brand;
	 
	 @ApiModelProperty(value = "版本号，如1.0.0")
	 @NotNull(message = "app版本号不能为空")
	 private String version;
	 
	 @ApiModelProperty("平台：0-ios 1-android")
	 @NotNull(message = "操作系统不能为空")
	 private String plat;
	 
	 @ApiModelProperty(value = "宽")
	 private String w;
	 
	 @ApiModelProperty(value = "build")
	 private String build;
	 
	 @ApiModelProperty(value = "channel")
	 private String channel;
	 
	 @ApiModelProperty(value = "token")
	 private String token;

}
