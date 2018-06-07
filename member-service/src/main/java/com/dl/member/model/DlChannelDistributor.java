package com.dl.member.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "dl_channel_distributor")
public class DlChannelDistributor {
	/**
	 * 
	 渠道分销者(店员)id
	 */
	@Id
	@Column(name = "channel_distributor_id")
	private Integer channelDistributorId;

	/**
	 * 所属渠道ID
	 */
	@Column(name = "channel_id")
	private Integer channelId;

	/**
	 * 用户Id(与用户表做关联)
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 店员名称
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 渠道分销号
	 */
	@Column(name = "channel_distributor_num")
	private String channelDistributorNum;

	/**
	 * 电话
	 */
	@Column(name = "mobile")
	private String mobile;

	/**
	 * 分销者佣金比例
	 */
	@Column(name = "distributor_commission_rate")
	private Double distributorCommissionRate;

	/**
	 * 所属渠道名称
	 */
	@Column(name = "channel_name")
	private String channelName;

	/**
	 * 添加时间
	 */
	@Column(name = "add_time")
	private Integer addTime;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 是否删除
	 */
	@Column(name = "deleted")
	private Integer deleted;
}