package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_user_recharge")
public class UserRecharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 充值
     */
    @Column(name = "recharge_sn")
    private String rechargeSn;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 充值/提现金额
     */
    private BigDecimal amount;

    @Column(name = "account_id")
    private Integer accountId;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 提现或充值状态,1-已完成，0-未完成
     */
    private Boolean status;

    /**
     * 类型0-提现 1-充值
     */
    @Column(name = "process_type")
    private Boolean processType;

    /**
     * 支付代码
     */
    @Column(name = "payment_code")
    private String paymentCode;

    /**
     * 支付方式名称
     */
    @Column(name = "payment_name")
    private String paymentName;

    /**
     * 付款时间
     */
    @Column(name = "pay_time")
    private Integer payTime;

    /**
     * 交易号
     */
    @Column(name = "payment_id")
    private String paymentId;

    /**
     * 赠送id
     */
    @Column(name = "donation_id")
    private String donationId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取充值
     *
     * @return recharge_sn - 充值
     */
    public String getRechargeSn() {
        return rechargeSn;
    }

    /**
     * 设置充值
     *
     * @param rechargeSn 充值
     */
    public void setRechargeSn(String rechargeSn) {
        this.rechargeSn = rechargeSn;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取充值/提现金额
     *
     * @return amount - 充值/提现金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置充值/提现金额
     *
     * @param amount 充值/提现金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return account_id
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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
     * 获取提现或充值状态,1-已完成，0-未完成
     *
     * @return status - 提现或充值状态,1-已完成，0-未完成
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置提现或充值状态,1-已完成，0-未完成
     *
     * @param status 提现或充值状态,1-已完成，0-未完成
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取类型0-提现 1-充值
     *
     * @return process_type - 类型0-提现 1-充值
     */
    public Boolean getProcessType() {
        return processType;
    }

    /**
     * 设置类型0-提现 1-充值
     *
     * @param processType 类型0-提现 1-充值
     */
    public void setProcessType(Boolean processType) {
        this.processType = processType;
    }

    /**
     * 获取支付代码
     *
     * @return payment_code - 支付代码
     */
    public String getPaymentCode() {
        return paymentCode;
    }

    /**
     * 设置支付代码
     *
     * @param paymentCode 支付代码
     */
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    /**
     * 获取支付方式名称
     *
     * @return payment_name - 支付方式名称
     */
    public String getPaymentName() {
        return paymentName;
    }

    /**
     * 设置支付方式名称
     *
     * @param paymentName 支付方式名称
     */
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    /**
     * 获取付款时间
     *
     * @return pay_time - 付款时间
     */
    public Integer getPayTime() {
        return payTime;
    }

    /**
     * 设置付款时间
     *
     * @param payTime 付款时间
     */
    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取交易号
     *
     * @return payment_id - 交易号
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * 设置交易号
     *
     * @param paymentId 交易号
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * 获取赠送id
     *
     * @return donation_id - 赠送id
     */
    public String getDonationId() {
        return donationId;
    }

    /**
     * 设置赠送id
     *
     * @param donationId 赠送id
     */
    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }
}