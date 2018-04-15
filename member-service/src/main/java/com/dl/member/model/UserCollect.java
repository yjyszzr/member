package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_user_collect")
public class UserCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文章id
     */
    @Column(name = "article_id")
    private String articleId;

    @Column(name = "article_title")
    private String articleTitle;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private String addTime;

    /**
     * 收藏来源
     */
    @Column(name = "collect_from")
    private String collectFrom;

    @Column(name = "dl_user_collectcol")
    private String dlUserCollectcol;

    @Column(name = "user_id")
    private Integer userId;

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
     * 获取文章id
     *
     * @return article_id - 文章id
     */
    public String getArticleId() {
        return articleId;
    }

    /**
     * 设置文章id
     *
     * @param articleId 文章id
     */
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    /**
     * @return article_title
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * @param articleTitle
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * 获取添加时间
     *
     * @return add_time - 添加时间
     */
    public String getAddTime() {
        return addTime;
    }

    /**
     * 设置添加时间
     *
     * @param addTime 添加时间
     */
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取收藏来源
     *
     * @return collect_from - 收藏来源
     */
    public String getCollectFrom() {
        return collectFrom;
    }

    /**
     * 设置收藏来源
     *
     * @param collectFrom 收藏来源
     */
    public void setCollectFrom(String collectFrom) {
        this.collectFrom = collectFrom;
    }

    /**
     * @return dl_user_collectcol
     */
    public String getDlUserCollectcol() {
        return dlUserCollectcol;
    }

    /**
     * @param dlUserCollectcol
     */
    public void setDlUserCollectcol(String dlUserCollectcol) {
        this.dlUserCollectcol = dlUserCollectcol;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}