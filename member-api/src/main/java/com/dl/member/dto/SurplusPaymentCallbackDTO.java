package com.dl.member.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SurplusPaymentCallbackDTO {

	/**
	 * 使用余额支付的金额
	 */
	@ApiModelProperty("使用余额支付的金额")
	private BigDecimal surplus;
	
	/**
	 * 使用的可提现余额
	 */
	@ApiModelProperty("使用的可提现余额")
	private BigDecimal userSurplus;
	
	/**
	 * 使用的不可提现余额
	 */
	@ApiModelProperty("使用的不可提现余额")
	private BigDecimal userSurplusLimit;
	
	/**
	 * 当前被冻结的余额
	 */
	@ApiModelProperty("当前被冻结的余额")
	private BigDecimal frozenMoney;
	
	/**
	 * 当前变动后的总余额
	 */
	@ApiModelProperty("当前变动后的总余额")
	private BigDecimal curBalance;
	

	
	/**
	 * 当前流水号
	 */
	@ApiModelProperty("当前流水号")
	private String accountSn;
	
	
}
