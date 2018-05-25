package com.dl.member.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "dl_cash_coupon_order")
public class DlCashCouponOrder {
	@Id
	@Column(name = "order_id")
	private Integer orderId;

	/**
	 * 订单号
	 */
	@Column(name = "order_sn")
	private String orderSn;

	/**
	 * 支付代码
	 */
	@Column(name = "pay_code")
	private String payCode;

	/**
	 * 支付名称
	 */
	@Column(name = "pay_name")
	private String payName;

	/**
	 * 订单实付金额
	 */
	@Column(name = "money_paid")
	private BigDecimal moneyPaid;

	/**
	 * 买家id
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 订单生成时间
	 */
	@Column(name = "add_time")
	private Integer addTime;

	/**
	 * 订单支付时间
	 */
	@Column(name = "pay_time")
	private Integer payTime;

	/**
	 * 是否删除
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 备注
	 */
	private String remarks;

}