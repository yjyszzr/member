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

	@ApiModelProperty("amt不能为空，回退金额")
	private BigDecimal amt;
}
