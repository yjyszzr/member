package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手机号信息
 * @author 
 *
 */
@ApiModel("消息信息")
@Data
public class DlMessageDTO {

	@ApiModelProperty("消息id")
    private Integer msgId;
	@ApiModelProperty("消息发送时间")
    private String sendTime;
	@ApiModelProperty("消息标题")
    private String title;
	@ApiModelProperty("消息类型：0通知1消息")
    private Integer msgType;
	@ApiModelProperty("消息接收人的id,-1代表所有人接收")
    private Integer receiver;
	@ApiModelProperty("消息接收者的手机号，可以为空")
    private String receiverMobile;
	@ApiModelProperty("消息业务类型：")
    private String objectType;
	@ApiModelProperty("是否已阅读")
    private Integer isRead;
	@ApiModelProperty("消息主题内容")
    private String content;
	@ApiModelProperty("消息附加信息")
    private String msgDesc;
	@ApiModelProperty("消息详情地址")
    private String msgUrl;
	@ApiModelProperty("消息主题附加")
    private String contentDesc;
}
