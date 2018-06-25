package com.dl.member.param;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemRollParam {
	@ApiModelProperty("订单号")
	@NotNull
	private String orderSn;
	
	@ApiModelProperty("用户id")
	private Integer userId;

	@ApiModelProperty("amt null全部提现，不为空只是回滚一个订单部分金额")
	private BigDecimal amt;
}
