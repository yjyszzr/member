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
    private Integer articleId;


    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Integer addTime;

    /**
     * 收藏来源
     */
    @Column(name = "collect_from")
    private String collectFrom;


    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "is_delete")
    private Integer isDelete;

    public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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
     * 获取文章id
     *
     * @return article_id - 文章id
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * 设置文章id
     *
     * @param articleId 文章id
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }


    /**
     * 获取添加时间
     *
     * @return add_time - 添加时间
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * 设置添加时间
     *
     * @param addTime 添加时间
     */
    public void setAddTime(Integer addTime) {
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