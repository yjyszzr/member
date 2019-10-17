package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("业务版本信息")
@Data
public class SwitchConfigParam {

    @ApiModelProperty("1-交易版 2-资讯版")
    private String businessType;

    @ApiModelProperty("0-ios 1-android")
    private String platform;

}