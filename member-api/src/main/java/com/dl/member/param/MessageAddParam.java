package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息添加参数
 *
 * @author 
 */
@Data
public class MessageAddParam {

    private String title;
    private String content;
    private String contentDesc;
    private String msgDesc;
    private Integer msgType;
    private Integer receiver;
    private String receiveMobile;
    private String objectType;
    private Integer sendTime;
    private Integer sender;
    private String msgUrl;
}
