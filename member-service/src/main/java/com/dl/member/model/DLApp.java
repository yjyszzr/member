package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_app")
public class DLApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_code_name")
    private Integer appCodeName;

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
     * @return app_name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return app_code_name
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
}