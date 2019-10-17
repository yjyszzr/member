package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_switch_config")
public class SwitchConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "turn_on")
    private Integer turnOn;

    /**
     * 1-交易版 2-资讯版
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 0 - ios 1- android  
     */
    private Integer platform;

    private String version;
    
    private String channel;
    
    

    public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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
     * @return turn_on
     */
    public Integer getTurnOn() {
        return turnOn;
    }

    /**
     * @param turnOn
     */
    public void setTurnOn(Integer turnOn) {
        this.turnOn = turnOn;
    }

    /**
     * 获取1-交易版 2-资讯版
     *
     * @return business_type - 1-交易版 2-资讯版
     */
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * 设置1-交易版 2-资讯版
     *
     * @param businessType 1-交易版 2-资讯版
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取0 - ios 1- android  
     *
     * @return platform - 0 - ios 1- android  
     */
    public Integer getPlatform() {
        return platform;
    }

    /**
     * 设置0 - ios 1- android  
     *
     * @param platform 0 - ios 1- android  
     */
    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    /**
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}