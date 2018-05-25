package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.Data;

@ApiModel("余额支付参数列表")
@Data
public class PaymentPageParam {
	@ApiModelProperty("我的余额")
	private BigDecimal myAmount;
	@ApiModelProperty("代金券价格")
	private BigDecimal cashCouponPrice;

}
