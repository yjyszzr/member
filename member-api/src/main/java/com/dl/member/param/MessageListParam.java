package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息列表参数
 *
 * @author 
 */
@Data
public class MessageListParam {

    @ApiModelProperty(value = "页码")
    private Integer page;
    @ApiModelProperty(value = "页面显示数量")
    private Integer size;
    @ApiModelProperty(value = "消息类型：0通知，1消息")
    private Integer msgType;
}
