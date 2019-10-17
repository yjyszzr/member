package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("渠道")
@Data
public class DlChannelDTO {
	@ApiModelProperty(value = "渠道Id")
	private Integer channelId;
	@ApiModelProperty(value = "渠道名称")
	private String channelName;

}