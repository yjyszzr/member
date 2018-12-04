package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("功能是否开启开关")
@Data
public class ServiceSwitchDTO {

    @ApiModelProperty("业务id")
    private Integer businessId;

    @ApiModelProperty("功能：0-关 1-开")
    private String turnOn;
}
