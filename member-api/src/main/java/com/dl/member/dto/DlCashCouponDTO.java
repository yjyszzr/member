package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;

@ApiModel("代金券")
@Data
public class DlCashCouponDTO {
	@ApiModelProperty(value = "代金券Id")
	private Integer cashCouponId;
	@ApiModelProperty(value = "代金券名称")
	private String cashCouponName;
	@ApiModelProperty(value = "代金券面额")
	private BigDecimal cashCouponDenomination;
	@ApiModelProperty(value = "代金券售价")
	private BigDecimal cashCouponPrice;
	@ApiModelProperty(value = "关联活动ID")
	private Integer activeId;
	@ApiModelProperty(value = "发行数量")
	private Integer cashCoupon_Num;
	@ApiModelProperty(value = "开始时间")
	private Integer startTime;
	@ApiModelProperty(value = "截止有效时间")
	private Integer endTime;
	@ApiModelProperty(value = "备注")
	private String remarks;
}