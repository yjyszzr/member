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

	@ApiModelProperty(value="消息标题",required=true)
    private String title;
	@ApiModelProperty(value="消息主题内容",required=true)
    private String content;
	@ApiModelProperty(value="消息主题附加",required=true)
    private String contentDesc;
	@ApiModelProperty(value="消息附加信息")
    private String msgDesc;
	@ApiModelProperty(value="消息类型：0通知1消息",required=true)
    private Integer msgType;
	@ApiModelProperty(value="消息接收人的id,-1代表所有人接收",required=true)
    private Integer receiver;
	@ApiModelProperty(value="消息接收者的手机号，可以为空")
    private String receiveMobile;
	@ApiModelProperty(value="消息业务类型：1:订单，2：提现",required=true)
    private Integer objectType;
	@ApiModelProperty(value="消息发送时间",required=true)
    private Integer sendTime;
	@ApiModelProperty(value="发送者")
    private Integer sender;
	@ApiModelProperty(value="消息详情地址")
    private String msgUrl;
	@ApiModelProperty(value="消息内容图片地焉")
	private String contentUrl;
}
