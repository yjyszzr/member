package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;

@Table(name = "dl_user_bonus")
public class UserBonus {
    /**
     * 用户红包编号
     */
    @Id
    @Column(name = "user_bonus_id")
    private Integer userBonusId;

    /**
     * 领取红包的会员编号
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 红包编号
     */
    @Column(name = "bonus_id")
    private Integer bonusId;

    @Column(name = "bonus_sn")
    private String bonusSn;

    /**
     * 红包面值
     */
    @Column(name = "bonus_price")
    private BigDecimal bonusPrice;

    /**
     * 领取时间
     */
    @Column(name = "receive_time")
    private Integer receiveTime;

    /**
     * 红包使用日期
     */
    @Column(name = "used_time")
    private Integer usedTime;

    /**
     * 红包开始使用时间
     */
    @Column(name = "start_time")
    private Integer startTime;

    /**
     * 红包结束使用时间
     */
    @Column(name = "end_time")
    private Integer endTime;

    /**
     * 创建时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    private String orderSn;

    /**
     * 红包使用状态 0-未使用 1-已使用 2已过期
     */
    @Column(name = "bonus_status")
    private Integer bonusStatus;

    /**
     * 是否可用
     */
    @Column(name = "is_delete")
    private Integer isDelete;
    
    /**
     * 使用范围
     */
    @Column(name = "use_range")
    private Integer useRange;
    
    /**
     * 最小订单金额
     */
    @Column(name = "min_goods_amount")
    private BigDecimal minGoodsAmount;
    
    /**
     * 支付日志id
     */
    @Column(name = "pay_log_id")
    private Integer payLogId;
    
    private Integer rechargeCardId;
    
    private BigDecimal rechargeCardRealValue;
    
    public BigDecimal getRechargeCardRealValue() {
		return rechargeCardRealValue;
	}

	public void setRechargeCardRealValue(BigDecimal rechargeCardRealValue) {
		this.rechargeCardRealValue = rechargeCardRealValue;
	}

	public Integer getRechargeCardId() {
		return rechargeCardId;
	}

	public void setRechargeCardId(Integer rechargeCardId) {
		this.rechargeCardId = rechargeCardId;
	}

	public Integer getPayLogId() {
		return payLogId;
	}

	public void setPayLogId(Integer payLogId) {
		this.payLogId = payLogId;
	}

	public Integer getUseRange() {
		return useRange;
	}

	public void setUseRange(Integer useRange) {
		this.useRange = useRange;
	}

	public BigDecimal getMinGoodsAmount() {
		return minGoodsAmount;
	}

	public void setMinGoodsAmount(BigDecimal minGoodsAmount) {
		this.minGoodsAmount = minGoodsAmount;
	}

	/**
     * 获取用户红包编号
     *
     * @return user_bonus_id - 用户红包编号
     */
    public Integer getUserBonusId() {
        return userBonusId;
    }

    /**
     * 设置用户红包编号
     *
     * @param userBonusId 用户红包编号
     */
    public void setUserBonusId(Integer userBonusId) {
        this.userBonusId = userBonusId;
    }

    /**
     * 获取领取红包的会员编号
     *
     * @return user_id - 领取红包的会员编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置领取红包的会员编号
     *
     * @param userId 领取红包的会员编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取红包编号
     *
     * @return bonus_id - 红包编号
     */
    public Integer getBonusId() {
        return bonusId;
    }

    /**
     * 设置红包编号
     *
     * @param bonusId 红包编号
     */
    public void setBonusId(Integer bonusId) {
        this.bonusId = bonusId;
    }

    /**
     * @return bonus_sn
     */
    public String getBonusSn() {
        return bonusSn;
    }

    /**
     * @param bonusSn
     */
    public void setBonusSn(String bonusSn) {
        this.bonusSn = bonusSn;
    }

    /**
     * 获取红包面值
     *
     * @return bonus_price - 红包面值
     */
    public BigDecimal getBonusPrice() {
        return bonusPrice;
    }

    /**
     * 设置红包面值
     *
     * @param bonusPrice 红包面值
     */
    public void setBonusPrice(BigDecimal bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    /**
     * 获取领取时间
     *
     * @return receive_time - 领取时间
     */
    public Integer getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置领取时间
     *
     * @param receiveTime 领取时间
     */
    public void setReceiveTime(Integer receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**
     * 获取红包使用日期
     *
     * @return used_time - 红包使用日期
     */
    public Integer getUsedTime() {
        return usedTime;
    }

    /**
     * 设置红包使用日期
     *
     * @param usedTime 红包使用日期
     */
    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    /**
     * 获取红包开始使用时间
     *
     * @return start_time - 红包开始使用时间
     */
    public Integer getStartTime() {
        return startTime;
    }

    /**
     * 设置红包开始使用时间
     *
     * @param startTime 红包开始使用时间
     */
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取红包结束使用时间
     *
     * @return end_time - 红包结束使用时间
     */
    public Integer getEndTime() {
        return endTime;
    }

    /**
     * 设置红包结束使用时间
     *
     * @param endTime 红包结束使用时间
     */
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取创建时间
     *
     * @return add_time - 创建时间
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * 设置创建时间
     *
     * @param addTime 创建时间
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取订单编号
     *
     * @return order_sn - 订单编号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单编号
     *
     * @param orderSn 订单编号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 获取红包使用状态 0-未使用 1-已使用 2已过期
     *
     * @return bonus_status - 红包使用状态 0-未使用 1-已使用 2已过期
     */
    public Integer getBonusStatus() {
        return bonusStatus;
    }

    /**
     * 设置红包使用状态 0-未使用 1-已使用 2已过期
     *
     * @param bonusStatus 红包使用状态 0-未使用 1-已使用 2已过期
     */
    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    /**
     * 获取是否可用
     *
     * @return is_delete - 是否可用
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否可用
     *
     * @param isDelete 是否可用
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}