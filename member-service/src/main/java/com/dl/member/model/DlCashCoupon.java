package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_cash_coupon")
public class DlCashCoupon {
    /**
     * Id
     */
    @Id
    @Column(name = "cash_coupon_id")
    private Integer cashCouponId;

    /**
     * 代金券名称
     */
    @Column(name = "cash_coupon_name")
    private String cashCouponName;

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
     * 关联的活动Id
     */
    @Column(name = "active_id")
    private Integer activeId;

    /**
     * 发行数量
     */
    @Column(name = "cash_couponnum")
    private Integer cashCouponnum;

    /**
     * 红包有效起始时间
     */
    @Column(name = "start_time")
    private Integer startTime;

    /**
     * 红包有效截至时间
     */
    @Column(name = "end_time")
    private Integer endTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 获取Id
     *
     * @return cash_coupon_id - Id
     */
    public Integer getCashCouponId() {
        return cashCouponId;
    }

    /**
     * 设置Id
     *
     * @param cashCouponId Id
     */
    public void setCashCouponId(Integer cashCouponId) {
        this.cashCouponId = cashCouponId;
    }

    /**
     * 获取代金券名称
     *
     * @return cash_coupon_name - 代金券名称
     */
    public String getCashCouponName() {
        return cashCouponName;
    }

    /**
     * 设置代金券名称
     *
     * @param cashCouponName 代金券名称
     */
    public void setCashCouponName(String cashCouponName) {
        this.cashCouponName = cashCouponName;
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
     * 获取关联的活动Id
     *
     * @return active_id - 关联的活动Id
     */
    public Integer getActiveId() {
        return activeId;
    }

    /**
     * 设置关联的活动Id
     *
     * @param activeId 关联的活动Id
     */
    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    /**
     * 获取发行数量
     *
     * @return cash_couponnum - 发行数量
     */
    public Integer getCashCouponnum() {
        return cashCouponnum;
    }

    /**
     * 设置发行数量
     *
     * @param cashCouponnum 发行数量
     */
    public void setCashCouponnum(Integer cashCouponnum) {
        this.cashCouponnum = cashCouponnum;
    }

    /**
     * 获取红包有效起始时间
     *
     * @return start_time - 红包有效起始时间
     */
    public Integer getStartTime() {
        return startTime;
    }

    /**
     * 设置红包有效起始时间
     *
     * @param startTime 红包有效起始时间
     */
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取红包有效截至时间
     *
     * @return end_time - 红包有效截至时间
     */
    public Integer getEndTime() {
        return endTime;
    }

    /**
     * 设置红包有效截至时间
     *
     * @param endTime 红包有效截至时间
     */
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
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