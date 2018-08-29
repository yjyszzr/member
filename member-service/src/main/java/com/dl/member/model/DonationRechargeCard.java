package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_donation_recharge_card")
public class DonationRechargeCard {
    /**
     * 充值卡id
     */
    @Id
    @Column(name = "recharge_card_id")
    private Integer rechargeCardId;

    /**
     * 充值卡名称
     */
    private String name;

    /**
     * 充值卡图片路径
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 0代表赠品可用,1代表赠品不可用
     */
    private Integer status;

    @Column(name = "add_user")
    private String addUser;

    /**
     * 创建时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    private String description;

    /**
     * 0代表未被删除,1代表被删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 充值卡实际价值
     */
    @Column(name = "real_value")
    private BigDecimal realValue;
 
    /**
     * 充值卡类型
     */
    @Column(name = "type")
    private Integer type;

    
    
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
     * 获取充值卡id
     *
     * @return recharge_card_id - 充值卡id
     */
    public Integer getRechargeCardId() {
        return rechargeCardId;
    }

    /**
     * 设置充值卡id
     *
     * @param rechargeCardId 充值卡id
     */
    public void setRechargeCardId(Integer rechargeCardId) {
        this.rechargeCardId = rechargeCardId;
    }

    /**
     * 获取充值卡名称
     *
     * @return name - 充值卡名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置充值卡名称
     *
     * @param name 充值卡名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取充值卡图片路径
     *
     * @return img_url - 充值卡图片路径
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置充值卡图片路径
     *
     * @param imgUrl 充值卡图片路径
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取0代表赠品可用,1代表赠品不可用
     *
     * @return status - 0代表赠品可用,1代表赠品不可用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0代表赠品可用,1代表赠品不可用
     *
     * @param status 0代表赠品可用,1代表赠品不可用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return add_user
     */
    public String getAddUser() {
        return addUser;
    }

    /**
     * @param addUser
     */
    public void setAddUser(String  addUser) {
        this.addUser = addUser;
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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取0代表未被删除,1代表被删除
     *
     * @return is_delete - 0代表未被删除,1代表被删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置0代表未被删除,1代表被删除
     *
     * @param isDelete 0代表未被删除,1代表被删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取充值卡实际价值
     *
     * @return real_value - 充值卡实际价值
     */
    public BigDecimal getRealValue() {
        return realValue;
    }

    /**
     * 设置充值卡实际价值
     *
     * @param realValue 充值卡实际价值
     */
    public void setRealValue(BigDecimal realValue) {
        this.realValue = realValue;
    }
}