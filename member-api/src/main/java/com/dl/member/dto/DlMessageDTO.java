package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 手机号信息
 * @author 
 *
 */
@ApiModel("消息信息")
@Data
public class DlMessageDTO {

    private Integer msgId;

    private String sendTime;

    private String title;

    private Integer msgType;

    private Integer receiver;

    private String receiverMobile;

    private String objectType;

    private Integer isRead;

    private String content;

    private String msgDesc;
    
    private String msgUrl;
    
    private String contentDesc;
}
