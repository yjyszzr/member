package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_user_qualification")
public class UserQualification {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 活动id
     */
    @Column(name = "act_id")
    private Integer actId;

    
    /**
     * 活动类型
     */
    @Column(name = "act_type")
    private Integer actType;
    
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 有资格-1 无资格-0
     */
    private Integer qualification;
    
    
    public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public Integer getActType() {
		return actType;
	}

	public void setActType(Integer actType) {
		this.actType = actType;
	}

	/**
     * 领取时间
     */
    private Integer receiveTime;
    
    public Integer getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Integer receiveTime) {
		this.receiveTime = receiveTime;
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
     * 获取有资格-1 无资格-0
     *
     * @return qualification - 有资格-1 无资格-0
     */
    public Integer getQualification() {
        return qualification;
    }

    /**
     * 设置有资格-1 无资格-0
     *
     * @param qualification 有资格-1 无资格-0
     */
    public void setQualification(Integer qualification) {
        this.qualification = qualification;
    }
}