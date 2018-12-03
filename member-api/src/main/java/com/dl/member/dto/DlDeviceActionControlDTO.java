package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
public class DlDeviceActionControlDTO {

    @ApiModelProperty(value = " mac地址")
    private String mac = "";

    @ApiModelProperty(value = "业务类型")
    private Integer busiType = 1;

    @Column(name = "弹出次数")
    private Integer alertTimes = 0;

    @Column(name = "添加时间")
    private Integer addTime;

    @Column(name = "更新时间")
    private Integer updateTime;

}