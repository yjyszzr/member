package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_sms_template")
public class SmsTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 短信模板id
     */
    @Column(name = "sms_template_id")
    private Integer smsTemplateId;

    /**
     * 短信供应商：1-聚合
     */
    @Column(name = "sms_supplier")
    private Integer smsSupplier;
    
    /**
     * 马夹包编码
     */
    @Column(name = "app_code_name")
    private Integer appCodeName;

    public Integer getAppCodeName() {
		return appCodeName;
	}

	public void setAppCodeName(Integer appCodeName) {
		this.appCodeName = appCodeName;
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
     * 获取短信模板id
     *
     * @return sms_template_id - 短信模板id
     */
    public Integer getSmsTemplateId() {
        return smsTemplateId;
    }

    /**
     * 设置短信模板id
     *
     * @param smsTemplateId 短信模板id
     */
    public void setSmsTemplateId(Integer smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    /**
     * 获取短信供应商：1-聚合
     *
     * @return sms_supplier - 短信供应商：1-聚合
     */
    public Integer getSmsSupplier() {
        return smsSupplier;
    }

    /**
     * 设置短信供应商：1-聚合
     *
     * @param smsSupplier 短信供应商：1-聚合
     */
    public void setSmsSupplier(Integer smsSupplier) {
        this.smsSupplier = smsSupplier;
    }
}