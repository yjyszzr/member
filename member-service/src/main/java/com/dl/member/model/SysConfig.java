package com.dl.member.model;

import java.math.BigDecimal;

import javax.persistence.*;

@Table(name = "dl_sys_config")
public class SysConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 值
     */
    private BigDecimal value;

    /**
     * 描述
     */
    private String describtion;
    
    /**
     * 数值文本
     */
    private String valueTxt;

    
    public String getValueTxt() {
		return valueTxt;
	}

	public void setValueTxt(String valueTxt) {
		this.valueTxt = valueTxt;
	}

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
     * 获取业务id
     *
     * @return business_id - 业务id
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * 设置业务id
     *
     * @param businessId 业务id
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * 获取值
     *
     * @return value - 值
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 获取描述
     *
     * @return describtion - 描述
     */
    public String getDescribtion() {
        return describtion;
    }

    /**
     * 设置描述
     *
     * @param describtion 描述
     */
    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }
}