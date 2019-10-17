package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_app_doc")
public class AppDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文案类型
     */
    private Integer classify;

    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 文案内容
     */
    private String content;

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
     * 获取文案类型
     *
     * @return classify - 文案类型
     */
    public Integer getClassify() {
        return classify;
    }

    /**
     * 设置文案类型
     *
     * @param classify 文案类型
     */
    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    /**
     * @return create_time
     */
    public Integer getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取文案内容
     *
     * @return content - 文案内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置文案内容
     *
     * @param content 文案内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}