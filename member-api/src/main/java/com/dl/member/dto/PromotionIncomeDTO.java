package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("推广收入")
@Data
public class PromotionIncomeDTO {
	@ApiModelProperty("时间")
	private String time;

	@ApiModelProperty("	注册人数")
	private Integer registerNum;

	@ApiModelProperty("	购彩金额")
	private Double lotteryAmount;

	@ApiModelProperty("收入")
	private Double income;

	@ApiModelProperty("id集合")
	private String ids;
}
