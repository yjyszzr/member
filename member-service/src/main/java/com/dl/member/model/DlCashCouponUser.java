package com.dl.member.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "dl_cash_coupon_user")
public class DlCashCouponUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 代金券Id
	 */
	@Column(name = "cash_coupon_id")
	private Integer cashCouponId;

	/**
	 * 用户Id
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 代金券面额
	 */
	@Column(name = "cash_coupon_denomination")
	private BigDecimal cashCouponDenomination;

	/**
	 * 价格
	 */
	@Column(name = "cash_coupon_price")
	private BigDecimal cashCouponPrice;

	/**
	 * 领取时间
	 */
	@Column(name = "receive_time")
	private Integer receiveTime;

	/**
	 * 备注
	 */
	private String remarks;
}