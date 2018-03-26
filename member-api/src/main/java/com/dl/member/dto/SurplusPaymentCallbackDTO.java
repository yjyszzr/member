package com.dl.member.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SurplusPaymentCallbackDTO {

	/**
	 * 使用余额支付的金额
	 */
	private BigDecimal surplus;
	
	/**
	 * 使用的可提现余额
	 */
	private BigDecimal userSurplus;
	
	/**
	 * 使用的不可提现余额
	 */
	private BigDecimal userSurplusLimit;
	
	/**
	 * 当前变动后的总余额
	 */
	private BigDecimal curBalance;
	
	/**
	 * 当前流水号
	 */
	private String accountSn;
	
	
}
