package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("我的推荐是否绑定过")
@Data
public class ChannelCustomerBindDTO {
	
	@ApiModelProperty("顾客id")
	private Integer distributorId;
	@ApiModelProperty("用户id")
	private Integer userId;
	@ApiModelProperty("手机号")
	private String mobile;
	@ApiModelProperty("顾客ip")
	private String consumerIp;

}
