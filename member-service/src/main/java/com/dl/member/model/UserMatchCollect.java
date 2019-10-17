package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_user_match_collect")
public class UserMatchCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "add_time")
    private Integer addTime;

    @Column(name = "collect_from")
    private String collectFrom;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_delete")
    private Integer isDelete;

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
     * @return match_id
     */
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * @param matchId
     */
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    /**
     * @return add_time
     */
    public Integer getAddTime() {
        return addTime;
    }

    /**
     * @param addTime
     */
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    /**
     * @return collect_from
     */
    public String getCollectFrom() {
        return collectFrom;
    }

    /**
     * @param collectFrom
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

    /**
     * @return is_delete
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}