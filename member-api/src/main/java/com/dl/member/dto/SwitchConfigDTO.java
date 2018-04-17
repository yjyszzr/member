package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("业务版本信息")
@Data
public class SwitchConfigDTO {
	@ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("1- 关 2- 开")
    private String turnOn;

    @ApiModelProperty("1-交易版 2-资讯版")
    private String businessType;

    @ApiModelProperty("0-ios 1-android")
    private String platform;

    @ApiModelProperty("版本号")
    private String version;

}