package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NoticeParam {

    @ApiModelProperty(value = "消息业务类型：5-店铺投注记录有同步的订单消息")
    private String objType;

    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
