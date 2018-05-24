package com.dl.member.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "dl_channel_consumer")
@Data
public class DlChannelConsumer {
	@Id
	@Column(name = "consumer_id")
	private Integer consumerId;

	/**
	 * 
	 渠道分销者(店员)id
	 */
	@Column(name = "channel_distributor_id")
	private Integer channelDistributorId;

	/**
	 * 用户Id(与用户表做关联)
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 消费者名称
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 电话
	 */
	private String mobile;

	/**
	 * 访问IP
	 */
	@Column(name = "consumer_ip")
	private String consumerIp;

	/**
	 * 扫码时间
	 */
	@Column(name = "add_time")
	private Integer addTime;
	/**
	 * 第一次登陆时间
	 */
	@Column(name = "frist_login_time")
	private Integer fristLoginTime;
	/**
	 * 设备类型
	 */
	@Column(name = "device_code")
	private String deviceCode;

	/**
	 * 是否删除
	 */
	private Integer deleted;
}