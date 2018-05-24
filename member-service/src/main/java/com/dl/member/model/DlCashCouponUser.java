package com.dl.member.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "dl_cash_coupon_user")
public class DlCashCouponUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 代金券Id
     */
    @Column(name = "cash_coupon_id")
    private Integer cashCouponId;

    /**
     * 用户Id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 代金券面额
     */
    @Column(name = "cash_coupon_denomination")
    private BigDecimal cashCouponDenomination;

    /**
     * 价格
     */
    @Column(name = "cash_coupon_price")
    private BigDecimal cashCouponPrice;

    /**
     * 领取时间
     */
    @Column(name = "receive_time")
    private Date receiveTime;

    /**
     * 备注
     */
    private String remarks;

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
     * 获取代金券Id
     *
     * @return cash_coupon_id - 代金券Id
     */
    public Integer getCashCouponId() {
        return cashCouponId;
    }

    /**
     * 设置代金券Id
     *
     * @param cashCouponId 代金券Id
     */
    public void setCashCouponId(Integer cashCouponId) {
        this.cashCouponId = cashCouponId;
    }

    /**
     * 获取用户Id
     *
     * @return user_id - 用户Id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户Id
     *
     * @param userId 用户Id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取代金券面额
     *
     * @return cash_coupon_denomination - 代金券面额
     */
    public BigDecimal getCashCouponDenomination() {
        return cashCouponDenomination;
    }

    /**
     * 设置代金券面额
     *
     * @param cashCouponDenomination 代金券面额
     */
    public void setCashCouponDenomination(BigDecimal cashCouponDenomination) {
        this.cashCouponDenomination = cashCouponDenomination;
    }

    /**
     * 获取价格
     *
     * @return cash_coupon_price - 价格
     */
    public BigDecimal getCashCouponPrice() {
        return cashCouponPrice;
    }

    /**
     * 设置价格
     *
     * @param cashCouponPrice 价格
     */
    public void setCashCouponPrice(BigDecimal cashCouponPrice) {
        this.cashCouponPrice = cashCouponPrice;
    }

    /**
     * 获取领取时间
     *
     * @return receive_time - 领取时间
     */
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置领取时间
     *
     * @param receiveTime 领取时间
     */
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}