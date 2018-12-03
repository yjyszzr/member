package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_device_action_control")
public class DlDeviceActionControl {
    @Id
    private Integer id;

    private String mac;

    @Column(name = "busi_type")
    private Integer busiType;

    @Column(name = "alert_times")
    private Integer alertTimes;

    @Column(name = "add_time")
    private Integer addTime;

    @Column(name = "update_time")
    private Integer updateTime;

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


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return busi_type
     */
    public Integer getBusiType() {
        return busiType;
    }

    /**
     * @param busiType
     */
    public void setBusiType(Integer busiType) {
        this.busiType = busiType;
    }

    /**
     * @return alert_times
     */
    public Integer getAlertTimes() {
        return alertTimes;
    }

    /**
     * @param alertTimes
     */
    public void setAlertTimes(Integer alertTimes) {
        this.alertTimes = alertTimes;
    }

    /**
     * @return add_time
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * @param addTime
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    /**
     * @return update_time
     */
    public Integer getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}