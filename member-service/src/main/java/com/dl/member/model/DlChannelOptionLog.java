package com.dl.member.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "dl_channel_option_log")
public class DlChannelOptionLog {
	@Id
	@Column(name = "option_id")
	private Integer optionId;

	/**
	 * 渠道Id
	 */
	@Column(name = "channel_id")
	private Integer channelId;

	/**
	 * 
	 渠道分销者(店员)id
	 */
	@Column(name = "distributor_id")
	private Integer distributorId;

	/**
	 * 店员用户Id(与用户表做关联)
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 店员名称
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 身份证号码
	 */
	@Column(name = "id_card_num")
	private Integer idCardNum;

	/**
	 * 真实姓名
	 */
	@Column(name = "true_name")
	private String trueName;

	/**
	 * 电话
	 */
	private String mobile;

	/**
	 * 操作节点 1 注册 2 购彩
	 */
	@Column(name = "operation_node")
	private Byte operationNode;

	/**
	 * 状态1 正常 2 冻结
	 */
	private Byte status;

	/**
	 * 操作金额
	 */
	@Column(name = "option_amount")
	private BigDecimal optionAmount;

	/**
	 * 注册时间
	 */
	@Column(name = "option_time")
	private Integer optionTime;

	/**
	 * 来源
	 */
	private String source;

}