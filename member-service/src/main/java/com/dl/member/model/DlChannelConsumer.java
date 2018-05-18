package com.dl.member.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_channel_consumer")
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
    private Date addTime;

    /**
     * 设备类型
     */
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 是否删除
     */
    private Byte deleted;

    /**
     * @return consumer_id
     */
    public Integer getConsumerId() {
        return consumerId;
    }

    /**
     * @param consumerId
     */
    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * 获取
渠道分销者(店员)id
     *
     * @return channel_distributor_id - 
渠道分销者(店员)id
     */
    public Integer getChannelDistributorId() {
        return channelDistributorId;
    }

    /**
     * 设置
渠道分销者(店员)id
     *
     * @param channelDistributorId 
渠道分销者(店员)id
     */
    public void setChannelDistributorId(Integer channelDistributorId) {
        this.channelDistributorId = channelDistributorId;
    }

    /**
     * 获取用户Id(与用户表做关联)
     *
     * @return user_id - 用户Id(与用户表做关联)
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户Id(与用户表做关联)
     *
     * @param userId 用户Id(与用户表做关联)
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取消费者名称
     *
     * @return user_name - 消费者名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置消费者名称
     *
     * @param userName 消费者名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取电话
     *
     * @return mobile - 电话
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置电话
     *
     * @param mobile 电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取访问IP
     *
     * @return consumer_ip - 访问IP
     */
    public String getConsumerIp() {
        return consumerIp;
    }

    /**
     * 设置访问IP
     *
     * @param consumerIp 访问IP
     */
    public void setConsumerIp(String consumerIp) {
        this.consumerIp = consumerIp;
    }

    /**
     * 获取扫码时间
     *
     * @return add_time - 扫码时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * 设置扫码时间
     *
     * @param addTime 扫码时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取设备类型
     *
     * @return device_code - 设备类型
     */
    public String getDeviceCode() {
        return deviceCode;
    }

    /**
     * 设置设备类型
     *
     * @param deviceCode 设备类型
     */
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    /**
     * 获取是否删除
     *
     * @return deleted - 是否删除
     */
    public Byte getDeleted() {
        return deleted;
    }

    /**
     * 设置是否删除
     *
     * @param deleted 是否删除
     */
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }
}