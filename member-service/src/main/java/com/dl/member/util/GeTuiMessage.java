package com.dl.member.util;

import com.dl.member.param.PushMessageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeTuiMessage {

	/*@ApiModelProperty(value="类型")
	private String type;
	@ApiModelProperty(value="一级id")
	private String id;
	@ApiModelProperty(value="二级id")
	private String subid;*/
	@ApiModelProperty(value="标题")
	private String title;
	@ApiModelProperty(value="描述")
	private String content;
	@ApiModelProperty(value="推送时间")
	private Integer pushTime;
	
	public GeTuiMessage(){}
	
	public GeTuiMessage(String title, String content, Integer pushTime){
		this.title = title;
		this.content = content;
		this.pushTime = pushTime;
	}

	public GeTuiMessage(PushMessageParam param) {
		this.title = param.getTitle();
		this.content = param.getContent();
		this.pushTime = param.getPushTime();
		/*this.type = param.getType();
		this.id = param.getId();
		this.subid = param.getSubid();*/
	}
}
