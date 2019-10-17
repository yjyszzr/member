package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_activity")
public class DLActivity {
    @Id
    @Column(name = "act_id")
    private Integer actId;

    /**
     * 活动名称
     */
    @Column(name = "act_name")
    private String actName;

    /**
     * 活动标题
     */
    @Column(name = "act_title")
    private String actTitle;

    /**
     * 活动类型
     */
    @Column(name = "act_type")
    private Integer actType;

    /**
     * 活动图片
     */
    @Column(name = "act_img")
    private String actImg;

    /**
     * 活动跳转url
     */
    @Column(name = "act_url")
    private String actUrl;

    /**
     * 活动开始时间
     */
    @Column(name = "start_time")
    private Integer startTime;

    /**
     * 活动结束时间
     */
    @Column(name = "end_time")
    private Integer endTime;

    /**
     * 活动状态 0-有效 1-无效
     */
    @Column(name = "is_finish")
    private Integer isFinish;

    /**
     * 限购数量
     */
    @Column(name = "purchase_num")
    private Integer purchaseNum;

    /**
     * 审核状态 0- 未通过 1-通过
     */
    private Boolean status;

    /**
     * 票种范围：0-全部 1-部分
     */
    @Column(name = "use_range")
    private Boolean useRange;

    /**
     * @return act_id
     */
    public Integer getActId() {
        return actId;
    }

    /**
     * @param actId
     */
    public void setActId(Integer actId) {
        this.actId = actId;
    }

    /**
     * 获取活动名称
     *
     * @return act_name - 活动名称
     */
    public String getActName() {
        return actName;
    }

    /**
     * 设置活动名称
     *
     * @param actName 活动名称
     */
    public void setActName(String actName) {
        this.actName = actName;
    }

    /**
     * 获取活动标题
     *
     * @return act_title - 活动标题
     */
    public String getActTitle() {
        return actTitle;
    }

    /**
     * 设置活动标题
     *
     * @param actTitle 活动标题
     */
    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    /**
     * 获取活动类型
     *
     * @return act_type - 活动类型
     */
    public Integer getActType() {
        return actType;
    }

    /**
     * 设置活动类型
     *
     * @param actType 活动类型
     */
    public void setActType(Integer actType) {
        this.actType = actType;
    }

    /**
     * 获取活动图片
     *
     * @return act_img - 活动图片
     */
    public String getActImg() {
        return actImg;
    }

    /**
     * 设置活动图片
     *
     * @param actImg 活动图片
     */
    public void setActImg(String actImg) {
        this.actImg = actImg;
    }

    /**
     * 获取活动跳转url
     *
     * @return act_url - 活动跳转url
     */
    public String getActUrl() {
        return actUrl;
    }

    /**
     * 设置活动跳转url
     *
     * @param actUrl 活动跳转url
     */
    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    /**
     * 获取活动开始时间
     *
     * @return start_time - 活动开始时间
     */
    public Integer getStartTime() {
        return startTime;
    }

    /**
     * 设置活动开始时间
     *
     * @param startTime 活动开始时间
     */
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取活动结束时间
     *
     * @return end_time - 活动结束时间
     */
    public Integer getEndTime() {
        return endTime;
    }

    /**
     * 设置活动结束时间
     *
     * @param endTime 活动结束时间
     */
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取活动状态 0-有效 1-无效
     *
     * @return is_finish - 活动状态 0-有效 1-无效
     */
    public Integer getIsFinish() {
        return isFinish;
    }

    /**
     * 设置活动状态 0-有效 1-无效
     *
     * @param isFinish 活动状态 0-有效 1-无效
     */
    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    /**
     * 获取限购数量
     *
     * @return purchase_num - 限购数量
     */
    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    /**
     * 设置限购数量
     *
     * @param purchaseNum 限购数量
     */
    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    /**
     * 获取审核状态 0- 未通过 1-通过
     *
     * @return status - 审核状态 0- 未通过 1-通过
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置审核状态 0- 未通过 1-通过
     *
     * @param status 审核状态 0- 未通过 1-通过
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取票种范围：0-全部 1-部分
     *
     * @return use_range - 票种范围：0-全部 1-部分
     */
    public Boolean getUseRange() {
        return useRange;
    }

    /**
     * 设置票种范围：0-全部 1-部分
     *
     * @param useRange 票种范围：0-全部 1-部分
     */
    public void setUseRange(Boolean useRange) {
        this.useRange = useRange;
    }
}