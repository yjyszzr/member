package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("收入明细")
@Data
public class IncomeDetailsDTO {

	@ApiModelProperty("时间")
	private String addTime;

	@ApiModelProperty("首次登陆时间")
	private String fristLoginTime;

	@ApiModelProperty("用户ID")
	private Integer userId;

	@ApiModelProperty("电话")
	private String mobile;

	@ApiModelProperty("	购彩金额")
	private Double lotteryAmount;

	@ApiModelProperty("店员ID")
	private Integer channelDistributorId;

	@ApiModelProperty("收入")
	private Double income;
}
