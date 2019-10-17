package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PushMessageParam {

	@ApiModelProperty(value="类型")
	private String type;
	@ApiModelProperty(value="一级id")
	private String id;
	@ApiModelProperty(value="二级id")
	private String subid;
	@ApiModelProperty(value="标题")
	private String title;
	@ApiModelProperty(value="描述")
	private String content;
	@ApiModelProperty(value="推送时间")
	private Integer pushTime;
	@ApiModelProperty(value="个推id")
	private String clientId;
}
