package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@ApiModel("我的推荐")
@Data
public class ChannelDistributorDTO {
	@ApiModelProperty("顾客id")
	private Integer consumerId;
	@ApiModelProperty("用户id")
	private Integer userId;
	@ApiModelProperty("手机号")
	private String mobile;
	@ApiModelProperty("顾客ip")
	private String consumerIp;
}
