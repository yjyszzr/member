package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_complain")
public class DlComplain {
    /**
     * 投诉ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 投诉者
     */
    private Integer complainer;

    /**
     * 投诉时间
     */
    @Column(name = "complain_time")
    private Integer complainTime;

    /**
     * 是否读取
     */
    @Column(name = "is_read")
    private Integer isRead;

    /**
     * 投诉内容
     */
    @Column(name = "complain_content")
    private String complainContent;

    /**
     * 获取投诉ID
     *
     * @return id - 投诉ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置投诉ID
     *
     * @param id 投诉ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取投诉者
     *
     * @return complainer - 投诉者
     */
    public Integer getComplainer() {
        return complainer;
    }

    /**
     * 设置投诉者
     *
     * @param complainer 投诉者
     */
    public void setComplainer(Integer complainer) {
        this.complainer = complainer;
    }

    /**
     * 获取投诉时间
     *
     * @return complain_time - 投诉时间
     */
    public Integer getComplainTime() {
        return complainTime;
    }

    /**
     * 设置投诉时间
     *
     * @param complainTime 投诉时间
     */
    public void setComplainTime(Integer complainTime) {
        this.complainTime = complainTime;
    }

    /**
     * 获取是否读取
     *
     * @return is_read - 是否读取
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * 设置是否读取
     *
     * @param isRead 是否读取
     */
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    /**
     * 获取投诉内容
     *
     * @return complain_content - 投诉内容
     */
    public String getComplainContent() {
        return complainContent;
    }

    /**
     * 设置投诉内容
     *
     * @param complainContent 投诉内容
     */
    public void setComplainContent(String complainContent) {
        this.complainContent = complainContent;
    }
}