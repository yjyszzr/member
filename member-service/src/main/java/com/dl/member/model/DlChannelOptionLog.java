package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

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
     * 操作节点 1 注册 2  购彩
     */
    @Column(name = "operation_node")
    private String operationNode;

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

    /**
     * @return option_id
     */
    public Integer getOptionId() {
        return optionId;
    }

    /**
     * @param optionId
     */
    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

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
     * 获取
渠道分销者(店员)id
     *
     * @return distributor_id - 
渠道分销者(店员)id
     */
    public Integer getDistributorId() {
        return distributorId;
    }

    /**
     * 设置
渠道分销者(店员)id
     *
     * @param distributorId 
渠道分销者(店员)id
     */
    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    /**
     * 获取店员用户Id(与用户表做关联)
     *
     * @return user_id - 店员用户Id(与用户表做关联)
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置店员用户Id(与用户表做关联)
     *
     * @param userId 店员用户Id(与用户表做关联)
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
     * 获取身份证号码
     *
     * @return id_card_num - 身份证号码
     */
    public Integer getIdCardNum() {
        return idCardNum;
    }

    /**
     * 设置身份证号码
     *
     * @param idCardNum 身份证号码
     */
    public void setIdCardNum(Integer idCardNum) {
        this.idCardNum = idCardNum;
    }

    /**
     * 获取真实姓名
     *
     * @return true_name - 真实姓名
     */
    public String getTrueName() {
        return trueName;
    }

    /**
     * 设置真实姓名
     *
     * @param trueName 真实姓名
     */
    public void setTrueName(String trueName) {
        this.trueName = trueName;
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
     * 获取操作节点 1 注册 2  购彩
     *
     * @return operation_node - 操作节点 1 注册 2  购彩
     */
    public String getOperationNode() {
        return operationNode;
    }

    /**
     * 设置操作节点 1 注册 2  购彩
     *
     * @param operationNode 操作节点 1 注册 2  购彩
     */
    public void setOperationNode(String operationNode) {
        this.operationNode = operationNode;
    }

    /**
     * 获取状态1 正常 2 冻结
     *
     * @return status - 状态1 正常 2 冻结
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态1 正常 2 冻结
     *
     * @param status 状态1 正常 2 冻结
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取操作金额
     *
     * @return option_amount - 操作金额
     */
    public BigDecimal getOptionAmount() {
        return optionAmount;
    }

    /**
     * 设置操作金额
     *
     * @param optionAmount 操作金额
     */
    public void setOptionAmount(BigDecimal optionAmount) {
        this.optionAmount = optionAmount;
    }

    /**
     * 获取注册时间
     *
     * @return option_time - 注册时间
     */
    public Integer getOptionTime() {
        return optionTime;
    }

    /**
     * 设置注册时间
     *
     * @param optionTime 注册时间
     */
    public void setOptionTime(Integer optionTime) {
        this.optionTime = optionTime;
    }

    /**
     * 获取来源
     *
     * @return source - 来源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置来源
     *
     * @param source 来源
     */
    public void setSource(String source) {
        this.source = source;
    }
}