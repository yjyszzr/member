package com.dl.member.model;

import javax.persistence.*;

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
    private String mobile;

    /**
     * 分销者佣金比例
     */
    @Column(name = "distributor_commission_rate")
    private String distributorCommissionRate;

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
    private String remark;

    /**
     * 是否删除
     */
    private Byte deleted;

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
     * 获取所属渠道ID
     *
     * @return channel_id - 所属渠道ID
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置所属渠道ID
     *
     * @param channelId 所属渠道ID
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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
     * 获取店员名称
     *
     * @return user_name - 店员名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置店员名称
     *
     * @param userName 店员名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取渠道分销号
     *
     * @return channel_distributor_num - 渠道分销号
     */
    public String getChannelDistributorNum() {
        return channelDistributorNum;
    }

    /**
     * 设置渠道分销号
     *
     * @param channelDistributorNum 渠道分销号
     */
    public void setChannelDistributorNum(String channelDistributorNum) {
        this.channelDistributorNum = channelDistributorNum;
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
     * 获取分销者佣金比例
     *
     * @return distributor_commission_rate - 分销者佣金比例
     */
    public String getDistributorCommissionRate() {
        return distributorCommissionRate;
    }

    /**
     * 设置分销者佣金比例
     *
     * @param distributorCommissionRate 分销者佣金比例
     */
    public void setDistributorCommissionRate(String distributorCommissionRate) {
        this.distributorCommissionRate = distributorCommissionRate;
    }

    /**
     * 获取所属渠道名称
     *
     * @return channel_name - 所属渠道名称
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * 设置所属渠道名称
     *
     * @param channelName 所属渠道名称
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
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