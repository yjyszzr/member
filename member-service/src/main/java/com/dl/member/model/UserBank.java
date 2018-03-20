package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_user_bank")
public class UserBank {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 持卡人姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 银行卡号
     */
    @Column(name = "card_no")
    private String cardNo;

    /**
     * 0-非当前默认,1-当前默认
     */
    private String status;
    
    /**
     * 银行卡logo
     */
    private String bankLogo;
    
	/**
     * 是否删除
     */
    private Integer isDelete;
    
    
    /**
     * 银行卡名称
     */
    private String bankName;
    

    public String getBankLogo() {
		return bankLogo;
	}

	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	
    public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	/**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取持卡人姓名
     *
     * @return real_name - 持卡人姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置持卡人姓名
     *
     * @param realName 持卡人姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取银行卡号
     *
     * @return card_no - 银行卡号
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置银行卡号
     *
     * @param cardNo 银行卡号
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 获取0-非当前默认,1-当前默认
     *
     * @return status - 0-非当前默认,1-当前默认
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置0-非当前默认,1-当前默认
     *
     * @param status 0-非当前默认,1-当前默认
     */
    public void setStatus(String status) {
        this.status = status;
    }
}