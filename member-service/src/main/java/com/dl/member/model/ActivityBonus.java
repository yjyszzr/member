package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_activity_bonus")
public class ActivityBonus {
    /**
     * 红包编号
     */
    @Id
    @Column(name = "bonus_id")
    private Integer bonusId;

    /**
     * 红包类型:1-注册送红包
     */
    @Column(name = "bonus_type")
    private Integer bonusType;

    /**
     * 红包活动名称
     */
    @Column(name = "bonus_name")
    private String bonusName;

    /**
     * 红包活动描述
     */
    @Column(name = "bonus_desc")
    private String bonusDesc;

    /**
     * 红包图片
     */
    @Column(name = "bonus_image")
    private String bonusImage;

    /**
     * 红包金额
     */
    @Column(name = "bonus_amount")
    private BigDecimal bonusAmount;

    /**
     * 会员领取次数 0-不限制
     */
    @Column(name = "receive_count")
    private Integer receiveCount;

    /**
     * 发放红包个数
     */
    @Column(name = "bonus_number")
    private Integer bonusNumber;

    /**
     * 使用范围 0-通用 1-指定分类 2-指定范围
     */
    @Column(name = "use_range")
    private Integer useRange;

    /**
     * 最小订单金额
     */
    @Column(name = "min_goods_amount")
    private BigDecimal minGoodsAmount;

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
     * 是否有效 0-失效 1-有效
     */
    @Column(name = "is_enable")
    private Integer isEnable;

    /**
     * 是否已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 红包添加时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 充值赠送概率
     */
    @Column(name = "recharge_chance")
    private BigDecimal rechargeChance;
 
    /**
     * 充值卡id
     */
    @Column(name = "recharge_card_id")
    private Integer rechargeCardId;
    
    /**
     * 兑换所需积分
     */
    @Column(name = "exchange_points")
    private Integer exchangePoints;

    /**
     * 兑换彩票数量
     */
    @Column(name = "exchange_goods_number")
    private Integer exchangeGoodsNumber;

    public Integer getRechargeCardId() {
		return rechargeCardId;
		
	}

	public void setRechargeCardId(Integer rechargeCardId) {
		this.rechargeCardId = rechargeCardId;
	}
    
    public BigDecimal getRechargeChance() {
		return rechargeChance;
	}

	public void setRechargeChance(BigDecimal rechargeChance) {
		this.rechargeChance = rechargeChance;
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
     * 获取红包类型:1-注册送红包
     *
     * @return bonus_type - 红包类型:1-注册送红包
     */
    public Integer getBonusType() {
        return bonusType;
    }

    /**
     * 设置红包类型:1-注册送红包
     *
     * @param bonusType 红包类型:1-注册送红包
     */
    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
    }

    /**
     * 获取红包活动名称
     *
     * @return bonus_name - 红包活动名称
     */
    public String getBonusName() {
        return bonusName;
    }

    /**
     * 设置红包活动名称
     *
     * @param bonusName 红包活动名称
     */
    public void setBonusName(String bonusName) {
        this.bonusName = bonusName;
    }

    /**
     * 获取红包活动描述
     *
     * @return bonus_desc - 红包活动描述
     */
    public String getBonusDesc() {
        return bonusDesc;
    }

    /**
     * 设置红包活动描述
     *
     * @param bonusDesc 红包活动描述
     */
    public void setBonusDesc(String bonusDesc) {
        this.bonusDesc = bonusDesc;
    }

    /**
     * 获取红包图片
     *
     * @return bonus_image - 红包图片
     */
    public String getBonusImage() {
        return bonusImage;
    }

    /**
     * 设置红包图片
     *
     * @param bonusImage 红包图片
     */
    public void setBonusImage(String bonusImage) {
        this.bonusImage = bonusImage;
    }

    /**
     * 获取红包金额
     *
     * @return bonus_amount - 红包金额
     */
    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    /**
     * 设置红包金额
     *
     * @param bonusAmount 红包金额
     */
    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    /**
     * 获取会员领取次数 0-不限制
     *
     * @return receive_count - 会员领取次数 0-不限制
     */
    public Integer getReceiveCount() {
        return receiveCount;
    }

    /**
     * 设置会员领取次数 0-不限制
     *
     * @param receiveCount 会员领取次数 0-不限制
     */
    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    /**
     * 获取发放红包个数
     *
     * @return bonus_number - 发放红包个数
     */
    public Integer getBonusNumber() {
        return bonusNumber;
    }

    /**
     * 设置发放红包个数
     *
     * @param bonusNumber 发放红包个数
     */
    public void setBonusNumber(Integer bonusNumber) {
        this.bonusNumber = bonusNumber;
    }

    /**
     * 获取使用范围 0-通用 1-指定分类 2-指定范围
     *
     * @return use_range - 使用范围 0-通用 1-指定分类 2-指定范围
     */
    public Integer getUseRange() {
        return useRange;
    }

    /**
     * 设置使用范围 0-通用 1-指定分类 2-指定范围
     *
     * @param useRange 使用范围 0-通用 1-指定分类 2-指定范围
     */
    public void setUseRange(Integer useRange) {
        this.useRange = useRange;
    }

    /**
     * 获取最小订单金额
     *
     * @return min_goods_amount - 最小订单金额
     */
    public BigDecimal getMinGoodsAmount() {
        return minGoodsAmount;
    }

    /**
     * 设置最小订单金额
     *
     * @param minGoodsAmount 最小订单金额
     */
    public void setMinGoodsAmount(BigDecimal minGoodsAmount) {
        this.minGoodsAmount = minGoodsAmount;
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
     * 获取是否有效 0-失效 1-有效
     *
     * @return is_enable - 是否有效 0-失效 1-有效
     */
    public Integer getIsEnable() {
        return isEnable;
    }

    /**
     * 设置是否有效 0-失效 1-有效
     *
     * @param isEnable 是否有效 0-失效 1-有效
     */
    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * 获取是否已删除
     *
     * @return is_delete - 是否已删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否已删除
     *
     * @param isDelete 是否已删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取红包添加时间
     *
     * @return add_time - 红包添加时间
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * 设置红包添加时间
     *
     * @param addTime 红包添加时间
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取兑换所需积分
     *
     * @return exchange_points - 兑换所需积分
     */
    public Integer getExchangePoints() {
        return exchangePoints;
    }

    /**
     * 设置兑换所需积分
     *
     * @param exchangePoints 兑换所需积分
     */
    public void setExchangePoints(Integer exchangePoints) {
        this.exchangePoints = exchangePoints;
    }

    /**
     * 获取兑换彩票数量
     *
     * @return exchange_goods_number - 兑换彩票数量
     */
    public Integer getExchangeGoodsNumber() {
        return exchangeGoodsNumber;
    }

    /**
     * 设置兑换彩票数量
     *
     * @param exchangeGoodsNumber 兑换彩票数量
     */
    public void setExchangeGoodsNumber(Integer exchangeGoodsNumber) {
        this.exchangeGoodsNumber = exchangeGoodsNumber;
    }
}