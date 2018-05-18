package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("收入排行榜")
@Data
public class IncomeRankingDTO {
	@ApiModelProperty("名次")
	private Integer ranking;
	@ApiModelProperty("推广员")
	private String distributorMobile;
	@ApiModelProperty("额外奖励")
	private Double extraReward;
	@ApiModelProperty("名次")
	private Double totalAmount;
}
