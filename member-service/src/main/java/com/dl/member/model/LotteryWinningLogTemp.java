package com.dl.member.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_winning_log_temp")
public class LotteryWinningLogTemp {
    /**
     * 中奖记录临时表id
     */
    @Id
    @Column(name = "winning_log_id")
    private Integer winningLogId;

    /**
     * 手机
     */
    private String phone;

    /**
     * 中奖金额
     */
    @Column(name = "winning_money")
    private BigDecimal winningMoney;

    /**
     * 是否显示 0-不显示 1-显示
     */
    @Column(name = "is_show")
    private Integer isShow;

    /**
     * 获取中奖记录临时表id
     *
     * @return winning_log_id - 中奖记录临时表id
     */
    public Integer getWinningLogId() {
        return winningLogId;
    }

    /**
     * 设置中奖记录临时表id
     *
     * @param winningLogId 中奖记录临时表id
     */
    public void setWinningLogId(Integer winningLogId) {
        this.winningLogId = winningLogId;
    }

    /**
     * 获取手机
     *
     * @return phone - 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取中奖金额
     *
     * @return winning_money - 中奖金额
     */
    public BigDecimal getWinningMoney() {
        return winningMoney;
    }

    /**
     * 设置中奖金额
     *
     * @param winningMoney 中奖金额
     */
    public void setWinningMoney(BigDecimal winningMoney) {
        this.winningMoney = winningMoney;
    }

    /**
     * 获取是否显示 0-不显示 1-显示
     *
     * @return is_show - 是否显示 0-不显示 1-显示
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * 设置是否显示 0-不显示 1-显示
     *
     * @param isShow 是否显示 0-不显示 1-显示
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}