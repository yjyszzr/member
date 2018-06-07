package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_channel")
public class DlChannel {
    /**
     * 渠道Id
     */
    @Id
    @Column(name = "channel_id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 活动Id
     */
    @Column(name = "act_id")
    private Integer actId;

    /**
     * 活动名称
     */
    @Column(name = "act_name")
    private String actName;

    /**
     * 渠道号
     */
    @Column(name = "channel_num")
    private String channelNum;

    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    /**
     * 渠道类型
     */
    @Column(name = "channel_type")
    private Byte channelType;

    /**
     * 联系人
     */
    @Column(name = "channel_contact")
    private String channelContact;

    /**
     * 渠道联系电话
     */
    @Column(name = "channel_mobile")
    private String channelMobile;

    /**
     * 渠道地址
     */
    @Column(name = "channel_address")
    private String channelAddress;

    /**
     * 渠道状态
     */
    @Column(name = "channel_status")
    private Byte channelStatus;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 是否删除
     */
    private Byte deleted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取渠道Id
     *
     * @return channel_id - 渠道Id
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置渠道Id
     *
     * @param channelId 渠道Id
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * 获取渠道名称
     *
     * @return channel_name - 渠道名称
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * 设置渠道名称
     *
     * @param channelName 渠道名称
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * 获取活动Id
     *
     * @return act_id - 活动Id
     */
    public Integer getActId() {
        return actId;
    }

    /**
     * 设置活动Id
     *
     * @param actId 活动Id
     */
    public void setActId(Integer actId) {
        this.actId = actId;
    }

    /**
     * 获取活动名称
     *
     * @return act_name - 活动名称
     */
    public String getActName() {
        return actName;
    }

    /**
     * 设置活动名称
     *
     * @param actName 活动名称
     */
    public void setActName(String actName) {
        this.actName = actName;
    }

    /**
     * 获取渠道号
     *
     * @return channel_num - 渠道号
     */
    public String getChannelNum() {
        return channelNum;
    }

    /**
     * 设置渠道号
     *
     * @param channelNum 渠道号
     */
    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }

    /**
     * 获取佣金比例
     *
     * @return commission_rate - 佣金比例
     */
    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    /**
     * 设置佣金比例
     *
     * @param commissionRate 佣金比例
     */
    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    /**
     * 获取渠道类型
     *
     * @return channel_type - 渠道类型
     */
    public Byte getChannelType() {
        return channelType;
    }

    /**
     * 设置渠道类型
     *
     * @param channelType 渠道类型
     */
    public void setChannelType(Byte channelType) {
        this.channelType = channelType;
    }

    /**
     * 获取联系人
     *
     * @return channel_contact - 联系人
     */
    public String getChannelContact() {
        return channelContact;
    }

    /**
     * 设置联系人
     *
     * @param channelContact 联系人
     */
    public void setChannelContact(String channelContact) {
        this.channelContact = channelContact;
    }

    /**
     * 获取渠道联系电话
     *
     * @return channel_mobile - 渠道联系电话
     */
    public String getChannelMobile() {
        return channelMobile;
    }

    /**
     * 设置渠道联系电话
     *
     * @param channelMobile 渠道联系电话
     */
    public void setChannelMobile(String channelMobile) {
        this.channelMobile = channelMobile;
    }

    /**
     * 获取渠道地址
     *
     * @return channel_address - 渠道地址
     */
    public String getChannelAddress() {
        return channelAddress;
    }

    /**
     * 设置渠道地址
     *
     * @param channelAddress 渠道地址
     */
    public void setChannelAddress(String channelAddress) {
        this.channelAddress = channelAddress;
    }

    /**
     * 获取渠道状态
     *
     * @return channel_status - 渠道状态
     */
    public Byte getChannelStatus() {
        return channelStatus;
    }

    /**
     * 设置渠道状态
     *
     * @param channelStatus 渠道状态
     */
    public void setChannelStatus(Byte channelStatus) {
        this.channelStatus = channelStatus;
    }

    /**
     * 获取添加时间
     *
     * @return add_time - 添加时间
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * 设置添加时间
     *
     * @param addTime 添加时间
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
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

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}