package com.dl.member.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dl_idfa_callback_info")
public class IDFACallBack {
	 /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
     * 应用在iTunes中的ID
     */
    @Column(name = "appid")
    private Integer appid;
    
    /**
     * IDFA号
     */
    @Column(name = "idfa")
    private String idfa;
    
    /**
     * 渠道
     */
    @Column(name = "source")
    private String source;
    
    /**
     * 广告商回调地址
     */
    @Column(name = "callback")
    private String callback;
    
    /**
     * 回调结果:1成功
     */
    @Column(name = "back_status")
    private Integer back_status;
    
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

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	 

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public Integer getBack_status() {
		return back_status;
	}

	public void setBack_status(Integer back_status) {
		this.back_status = back_status;
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
