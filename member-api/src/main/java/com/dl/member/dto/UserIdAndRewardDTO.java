package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserIdAndRewardDTO {

	@ApiModelProperty(value = "用户id")
	private Integer userId;

	@ApiModelProperty(value = "中奖金额")
	private BigDecimal reward;

	@ApiModelProperty(value = "订单号")
	private String orderSn;

	@ApiModelProperty(value = "投注金额")
	private String betMoney;

	@ApiModelProperty(value = "投注时间")
	private String betTime;

	@ApiModelProperty(value = "备注")
	private String note;

	@ApiModelProperty(value = "不需要传递：当前用户的可提现余额")
	private BigDecimal userMoney;
	@ApiModelProperty(value = "彩种类型")
	private Integer lotteryClassifyId;

}
