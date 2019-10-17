package com.dl.member.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "dl_cash_coupon")
public class DlCashCoupon {
	/**
	 * Id
	 */
	@Id
	@Column(name = "cash_coupon_id")
	private Integer cashCouponId;

	/**
	 * 代金券名称
	 */
	@Column(name = "cash_coupon_name")
	private String cashCouponName;

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
	 * 关联的活动Id
	 */
	@Column(name = "active_id")
	private Integer activeId;

	/**
	 * 发行数量
	 */
	@Column(name = "cash_coupon_num")
	private Integer cashCouponNum;

	/**
	 * 红包有效起始时间
	 */
	@Column(name = "start_time")
	private Integer startTime;

	/**
	 * 红包有效截至时间
	 */
	@Column(name = "end_time")
	private Integer endTime;

	/**
	 * 备注
	 */
	private String remarks;
}