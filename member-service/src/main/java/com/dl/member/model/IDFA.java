package com.dl.member.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_idfa_info")
public class IDFA {
	 /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer user_id;
    
    /**
     * IDFA号
     */
    @Column(name = "idfa")
    private String idfa;
    
    /**
     * 来源:0-普通用户,1-广告商推广
     */
    @Column(name = "idfa_from")
    private Integer idfa_from;
    
    /**
     * 创建时间
     */
    @Column(name = "creat_time")
    private Long creat_time;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date update_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
 
	public Integer getIdfa_from() {
		return idfa_from;
	}

	public void setIdfa_from(Integer idfa_from) {
		this.idfa_from = idfa_from;
	}

	public Long getCreat_time() {
		return creat_time;
	}

	public void setCreat_time(Long creat_time) {
		this.creat_time = creat_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
    
    
}
