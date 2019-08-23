package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户信息
 * @author zhangzirong
 *
 */
@ApiModel("用户信息")
@Data
public class UserDTO {

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号码
     */
    private Integer userId;

    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 手机号码未替换
     */
    private String realmobile;

    private String email;
    
    private String realName;

    private String realInfo;

    private String isSuperWhite = "0";
    
    /**
     * 登录密码
     */
    private String password;

    private String salt;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别:0-未知,1-男,2-女
     */
    private Boolean sex;

    /**
     * 出生日期
     */
    private Integer birthday;

    /**
     * 充值开关
     */
    private String recharegeTurnOn;


    /**
     *提现开关
     */
    private String withdrawTurnOn;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 头像
     */
    private String headimg;

    /**
     * 可提现余额(中的钱)
     */
    @Column(name = "user_money")
    private String userMoney;

    /**
     * 不可提现余额(充值的钱)
     */
    @Column(name = "user_money_limit")
    private String userMoneyLimit;
    
    /**
     * 用户总余额
     */
    private String totalMoney;
    
    
    /**
     * 用户余额
     */
    private String balance;

    /**
     * 冻结金额
     */
    @Column(name = "frozen_money")
    private BigDecimal frozenMoney;

    /**
     * 消费积分
     */
    @Column(name = "pay_point")
    private Integer payPoint;

    /**
     * 成长值
     */
    @Column(name = "rank_point")
    private Integer rankPoint;

    /**
     * 注册时间
     */
    @Column(name = "reg_time")
    private Integer regTime;

    /**
     * 注册IP地址
     */
    @Column(name = "reg_ip")
    private String regIp;


    /**
     * 最近登录时间
     */
    @Column(name = "last_time")
    private Integer lastTime;

    /**
     * 最近登录IP地址
     */
    @Column(name = "last_ip")
    private String lastIp;

    /**
     * 手机运营商
     */
    @Column(name = "mobile_supplier")
    private String mobileSupplier;

    /**
     * 归属省
     */
    @Column(name = "mobile_province")
    private String mobileProvince;
    /**
     * 短链接地址（冗余字段借用）
     */
    @Column(name = "province")
    private String province;

    /**
     * 归属市
     */
    @Column(name = "mobile_city")
    private String mobileCity;

    /**
     * 注册来源
     */
    @Column(name = "reg_from")
    private String regFrom;

    /**
     * 余额支付密码
     */
    @Column(name = "surplus_password")
    private String surplusPassword;

    /**
     * 支付密码盐
     */
    @Column(name = "pay_pwd_salt")
    private String payPwdSalt;

    /**
     * 用户状态:0-正常,1-被锁定,2-被冻结
     */
    @Column(name = "user_status")
    private Integer userStatus;

    /**
     * 密码错误次数
     */
    @Column(name = "pass_wrong_count")
    private Integer passWrongCount;

    private String merchantNo;
    private String merchantPass;
    
    /**
     * 用户持有有效卡券数量
     */
    private Integer bonusNumber;
    /**
     * 邀请人ID
     * @return
     */
    private Integer parentUserId;
    
    /**
     * 邀请奖励是否已生效
     * @return
     */
    private Integer isStatus;

    /**
     * 推广连接
     * @return
     */
    public String inviteUrl;

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 用户类型
     */
    @Column(name = "user_type")
    private Boolean userType;

    /**
     * 是否通过实名认证
     */
    @Column(name = "is_real")
    private String isReal;

    /**
     * 会员备注
     */
    @Column(name = "user_remark")
    private String userRemark;
    
    @ApiModelProperty("1已有密码0没有密码")
    private int hasPass=1;
    

    private List<com.dl.member.dto.ActivityDTO> activityDTOList;
 
    public String getRealInfo() {
        return realInfo;
    }

    public void setRealInfo(String realInfo) {
        this.realInfo = realInfo;
    }  
    
    /**
     * 获取活动集合
     *
     */
    public List<com.dl.member.dto.ActivityDTO> getActivityDTOList() {
        return activityDTOList;
    }

    /**
     * 设置活动集合
     *
     */
    public void setActivityDTOList(List<com.dl.member.dto.ActivityDTO> activityDTOList) {
        this.activityDTOList = activityDTOList;
    }



    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    public String getRecharegeTurnOn() {
        return recharegeTurnOn;
    }

    public void setRecharegeTurnOn(String recharegeTurnOn) {
        this.recharegeTurnOn = recharegeTurnOn;
    }

    public String getWithdrawTurnOn() {
        return withdrawTurnOn;
    }

    public void setWithdrawTurnOn(String withdrawTurnOn) {
        this.withdrawTurnOn = withdrawTurnOn;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取手机号码
     *
     * @return mobile - 手机号码
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     *
     * @param mobile 手机号码
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取登录密码
     *
     * @return password - 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置登录密码
     *
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取性别:0-未知,1-男,2-女
     *
     * @return sex - 性别:0-未知,1-男,2-女
     */
    public Boolean getSex() {
        return sex;
    }

    /**
     * 设置性别:0-未知,1-男,2-女
     *
     * @param sex 性别:0-未知,1-男,2-女
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    /**
     * 获取出生日期
     *
     * @return birthday - 出生日期
     */
    public Integer getBirthday() {
        return birthday;
    }

    /**
     * 设置出生日期
     *
     * @param birthday 出生日期
     */
    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取详细地址
     *
     * @return detail_address - 详细地址
     */
    public String getDetailAddress() {
        return detailAddress;
    }

    /**
     * 设置详细地址
     *
     * @param detailAddress 详细地址
     */
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    /**
     * 获取头像
     *
     * @return headimg - 头像
     */
    public String getHeadimg() {
        return headimg;
    }

    /**
     * 设置头像
     *
     * @param headimg 头像
     */
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    /**
     * 获取可提现余额(中的钱)
     *
     * @return user_money - 可提现余额(中的钱)
     */
    public String getUserMoney() {
        return userMoney;
    }

    /**
     * 设置可提现余额(中的钱)
     *
     * @param userMoney 可提现余额(中的钱)
     */
    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    /**
     * 获取不可提现余额(充值的钱)
     *
     * @return user_money_limit - 不可提现余额(充值的钱)
     */
    public String getUserMoneyLimit() {
        return userMoneyLimit;
    }

    /**
     * 设置不可提现余额(充值的钱)
     *
     * @param userMoneyLimit 不可提现余额(充值的钱)
     */
    public void setUserMoneyLimit(String userMoneyLimit) {
        this.userMoneyLimit = userMoneyLimit;
    }

    /**
     * 获取冻结金额
     *
     * @return frozen_money - 冻结金额
     */
    public BigDecimal getFrozenMoney() {
        return frozenMoney;
    }

    /**
     * 设置冻结金额
     *
     * @param frozenMoney 冻结金额
     */
    public void setFrozenMoney(BigDecimal frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    /**
     * 获取消费积分
     *
     * @return pay_point - 消费积分
     */
    public Integer getPayPoint() {
        return payPoint;
    }

    /**
     * 设置消费积分
     *
     * @param payPoint 消费积分
     */
    public void setPayPoint(Integer payPoint) {
        this.payPoint = payPoint;
    }

    /**
     * 获取成长值
     *
     * @return rank_point - 成长值
     */
    public Integer getRankPoint() {
        return rankPoint;
    }

    /**
     * 设置成长值
     *
     * @param rankPoint 成长值
     */
    public void setRankPoint(Integer rankPoint) {
        this.rankPoint = rankPoint;
    }

    /**
     * 获取注册时间
     *
     * @return reg_time - 注册时间
     */
    public Integer getRegTime() {
        return regTime;
    }

    /**
     * 设置注册时间
     *
     * @param regTime 注册时间
     */
    public void setRegTime(Integer regTime) {
        this.regTime = regTime;
    }

    /**
     * 获取注册IP地址
     *
     * @return reg_ip - 注册IP地址
     */
    public String getRegIp() {
        return regIp;
    }

    /**
     * 设置注册IP地址
     *
     * @param regIp 注册IP地址
     */
    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    /**
     * 获取最近登录时间
     *
     * @return last_time - 最近登录时间
     */
    public Integer getLastTime() {
        return lastTime;
    }

    /**
     * 设置最近登录时间
     *
     * @param lastTime 最近登录时间
     */
    public void setLastTime(Integer lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 获取最近登录IP地址
     *
     * @return last_ip - 最近登录IP地址
     */
    public String getLastIp() {
        return lastIp;
    }

    /**
     * 设置最近登录IP地址
     *
     * @param lastIp 最近登录IP地址
     */
    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    /**
     * 获取手机运营商
     *
     * @return mobile_supplier - 手机运营商
     */
    public String getMobileSupplier() {
        return mobileSupplier;
    }

    /**
     * 设置手机运营商
     *
     * @param mobileSupplier 手机运营商
     */
    public void setMobileSupplier(String mobileSupplier) {
        this.mobileSupplier = mobileSupplier;
    }

    /**
     * 获取归属省
     *
     * @return mobile_province - 归属省
     */
    public String getMobileProvince() {
        return mobileProvince;
    }

    /**
     * 设置归属省
     *
     * @param mobileProvince 归属省
     */
    public void setMobileProvince(String mobileProvince) {
        this.mobileProvince = mobileProvince;
    }

    /**
     * 获取归属市
     *
     * @return mobile_city - 归属市
     */
    public String getMobileCity() {
        return mobileCity;
    }

    /**
     * 设置归属市
     *
     * @param mobileCity 归属市
     */
    public void setMobileCity(String mobileCity) {
        this.mobileCity = mobileCity;
    }

    /**
     * 获取注册来源
     *
     * @return reg_from - 注册来源
     */
    public String getRegFrom() {
        return regFrom;
    }

    /**
     * 设置注册来源
     *
     * @param regFrom 注册来源
     */
    public void setRegFrom(String regFrom) {
        this.regFrom = regFrom;
    }

    /**
     * 获取余额支付密码
     *
     * @return surplus_password - 余额支付密码
     */
    public String getSurplusPassword() {
        return surplusPassword;
    }

    /**
     * 设置余额支付密码
     *
     * @param surplusPassword 余额支付密码
     */
    public void setSurplusPassword(String surplusPassword) {
        this.surplusPassword = surplusPassword;
    }

    /**
     * 获取支付密码盐
     *
     * @return pay_pwd_salt - 支付密码盐
     */
    public String getPayPwdSalt() {
        return payPwdSalt;
    }

    /**
     * 设置支付密码盐
     *
     * @param payPwdSalt 支付密码盐
     */
    public void setPayPwdSalt(String payPwdSalt) {
        this.payPwdSalt = payPwdSalt;
    }

    /**
     * 获取用户状态:0-正常,1-被锁定,2-被冻结
     *
     * @return status - 用户状态:0-正常,1-被锁定,2-被冻结
     */
    public Integer getUserStatus() {
        return userStatus;
    }

    /**
     * 设置用户状态:0-正常,1-被锁定,2-被冻结
     *
     * @param status 用户状态:0-正常,1-被锁定,2-被冻结
     */
    public void setUserStatus(Integer status) {
        this.userStatus = status;
    }

    /**
     * 获取密码错误次数
     *
     * @return pass_wrong_count - 密码错误次数
     */
    public Integer getPassWrongCount() {
        return passWrongCount;
    }

    /**
     * 设置密码错误次数
     *
     * @param passWrongCount 密码错误次数
     */
    public void setPassWrongCount(Integer passWrongCount) {
        this.passWrongCount = passWrongCount;
    }

    /**
     * 获取用户类型
     *
     * @return user_type - 用户类型
     */
    public Boolean getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public void setUserType(Boolean userType) {
        this.userType = userType;
    }

    /**
     * 获取是否通过实名认证
     *
     * @return is_real - 是否通过实名认证
     */
    public String getIsReal() {
        return isReal;
    }

    /**
     * 设置是否通过实名认证
     *
     * @param isReal 是否通过实名认证
     */
    public void setIsReal(String isReal) {
        this.isReal = isReal;
    }

    /**
     * 获取会员备注
     *
     * @return user_remark - 会员备注
     */
    public String getUserRemark() {
        return userRemark;
    }

    /**
     * 设置会员备注
     *
     * @param userRemark 会员备注
     */
    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }
    
    
    
}