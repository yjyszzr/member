package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_user_real")
public class UserReal {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "real_id")
    private Integer realId;

    /**
     * 用户基本信息ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 身份证号码
     */
    @Column(name = "id_code")
    private String idCode;

    /**
     * 身份证正面照
     */
    @Column(name = "card_pic1")
    private String cardPic1;

    /**
     * 身份证背面照
     */
    @Column(name = "card_pic2")
    private String cardPic2;

    /**
     * 本人手持身份证正面照
     */
    @Column(name = "card_pic3")
    private String cardPic3;

    /**
     * 现居住地址
     */
    @Column(name = "address_now")
    private String addressNow;

    /**
     * 详细地址
     */
    @Column(name = "address_info")
    private String addressInfo;

    /**
     * 是否通过实名认证
     */
    private String status;

    /**
     * 申请时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 申请时间
     */
    @Column(name = "is_delete")
    private Integer isDelete;
    
    
    /**
     * 拒绝原因
     */
    private String reason;

    
    
    public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/**
     * 获取主键ID
     *
     * @return real_id - 主键ID
     */
    public Integer getRealId() {
        return realId;
    }

    /**
     * 设置主键ID
     *
     * @param realId 主键ID
     */
    public void setRealId(Integer realId) {
        this.realId = realId;
    }

    /**
     * 获取用户基本信息ID
     *
     * @return user_id - 用户基本信息ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户基本信息ID
     *
     * @param userId 用户基本信息ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取真实姓名
     *
     * @return real_name - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取身份证号码
     *
     * @return id_code - 身份证号码
     */
    public String getIdCode() {
        return idCode;
    }

    /**
     * 设置身份证号码
     *
     * @param idCode 身份证号码
     */
    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    /**
     * 获取身份证正面照
     *
     * @return card_pic1 - 身份证正面照
     */
    public String getCardPic1() {
        return cardPic1;
    }

    /**
     * 设置身份证正面照
     *
     * @param cardPic1 身份证正面照
     */
    public void setCardPic1(String cardPic1) {
        this.cardPic1 = cardPic1;
    }

    /**
     * 获取身份证背面照
     *
     * @return card_pic2 - 身份证背面照
     */
    public String getCardPic2() {
        return cardPic2;
    }

    /**
     * 设置身份证背面照
     *
     * @param cardPic2 身份证背面照
     */
    public void setCardPic2(String cardPic2) {
        this.cardPic2 = cardPic2;
    }

    /**
     * 获取本人手持身份证正面照
     *
     * @return card_pic3 - 本人手持身份证正面照
     */
    public String getCardPic3() {
        return cardPic3;
    }

    /**
     * 设置本人手持身份证正面照
     *
     * @param cardPic3 本人手持身份证正面照
     */
    public void setCardPic3(String cardPic3) {
        this.cardPic3 = cardPic3;
    }

    /**
     * 获取现居住地址
     *
     * @return address_now - 现居住地址
     */
    public String getAddressNow() {
        return addressNow;
    }

    /**
     * 设置现居住地址
     *
     * @param addressNow 现居住地址
     */
    public void setAddressNow(String addressNow) {
        this.addressNow = addressNow;
    }

    /**
     * 获取详细地址
     *
     * @return address_info - 详细地址
     */
    public String getAddressInfo() {
        return addressInfo;
    }

    /**
     * 设置详细地址
     *
     * @param addressInfo 详细地址
     */
    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    /**
     * 获取是否通过实名认证
     *
     * @return status - 是否通过实名认证
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置是否通过实名认证
     *
     * @param status 是否通过实名认证
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取申请时间
     *
     * @return add_time - 申请时间
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * 设置申请时间
     *
     * @param addTime 申请时间
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取拒绝原因
     *
     * @return reason - 拒绝原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置拒绝原因
     *
     * @param reason 拒绝原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}