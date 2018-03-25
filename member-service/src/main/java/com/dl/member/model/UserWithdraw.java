package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_user_withdraw")
public class UserWithdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 提现单号
     */
    @Column(name = "withdrawal_sn")
    private String withdrawalSn;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 提现金额
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
     * 提现状态,1-已完成，0-未完成
     */
    private String status;

    /**
     * 支付代码
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 支付方式名称
     */
    @Column(name = "card_no")
    private String cardNo;

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
     * 获取提现单号
     *
     * @return withdrawal_sn - 提现单号
     */
    public String getWithdrawalSn() {
        return withdrawalSn;
    }

    /**
     * 设置提现单号
     *
     * @param withdrawalSn 提现单号
     */
    public void setWithdrawalSn(String withdrawalSn) {
        this.withdrawalSn = withdrawalSn;
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
     * 获取提现金额
     *
     * @return amount - 提现金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置提现金额
     *
     * @param amount 提现金额
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
     * 获取提现状态,1-已完成，0-未完成
     *
     * @return status - 提现状态,1-已完成，0-未完成
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置提现状态,1-已完成，0-未完成
     *
     * @param status 提现状态,1-已完成，0-未完成
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取支付代码
     *
     * @return real_name - 支付代码
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置支付代码
     *
     * @param realName 支付代码
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取支付方式名称
     *
     * @return card_no - 支付方式名称
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置支付方式名称
     *
     * @param cardNo 支付方式名称
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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
}