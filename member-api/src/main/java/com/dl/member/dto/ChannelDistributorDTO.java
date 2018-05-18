package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@ApiModel("我的推荐")
@Data
public class ChannelDistributorDTO {
	@ApiModelProperty("邀请用户量")
	Integer inviteNum;
	@ApiModelProperty("总购彩金额")
	Double bettingTotalAmount;
	@ApiModelProperty("收入排行榜")
	List<IncomeRankingDTO> channelDistributorList;
}
