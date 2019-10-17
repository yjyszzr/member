package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_app_update_log")
public class DLAppUpdateLog {
	@Id
	@Column(name = "id")
    private Integer id;

    @Column(name = "app_code_name")
    private Integer appCodeName;

    private String version;
    
    @Column(name = "channel")
    private String channel;
    
    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "update_log")
    private String updateLog;

    @Column(name = "update_time")
    private Integer updateTime;

    @Column(name = "update_install")
    private Integer updateInstall;

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
     * @return appCodeName
     */
    public Integer getAppCodeName() {
        return appCodeName;
    }

    /**
     * @param appCodeName
     */
    public void setAppCodeName(Integer appCodeName) {
        this.appCodeName = appCodeName;
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

    /**
     * @return download_url
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * @param downloadUrl
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * @return update_log
     */
    public String getUpdateLog() {
        return updateLog;
    }
    
    public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
     * @param updateLog
     */
    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
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

    /**
     * @return update_install
     */
    public Integer getUpdateInstall() {
        return updateInstall;
    }

    /**
     * @param updateInstall
     */
    public void setUpdateInstall(Integer updateInstall) {
        this.updateInstall = updateInstall;
    }
}