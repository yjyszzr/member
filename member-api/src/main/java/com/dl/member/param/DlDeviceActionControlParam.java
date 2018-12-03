package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class DlDeviceActionControlParam {

    @ApiModelProperty(value = "mac地址")
    private String mac;

    @ApiModelProperty(value = "业务类型：1.首页开屏图的控制")
    private Integer busiType;

    @ApiModelProperty(value = "弹出次数")
    private Integer alertTimes;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "修改时间")
    private Integer updateTime;

}
