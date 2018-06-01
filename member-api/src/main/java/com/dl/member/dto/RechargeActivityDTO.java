package com.dl.member.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值活动")
@Data
public class RechargeActivityDTO {
	
	@ApiModelProperty(value="充值卡列表")
	private List<DonationRechargeCardDTO> rechargeCardList;
	@ApiModelProperty(value="充值活动开始时间")
	private Integer startTime;
	@ApiModelProperty(value="充值活动结束时间")
	private Integer endTime;

	
}
