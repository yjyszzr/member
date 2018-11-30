package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_user_login_log")
public class UserLoginLog {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 登录类型 0 mobile，1 wx，2 qq 
     */
    @Column(name = "login_type")
    private Integer loginType;

    /**
     * 登录结果 0 成功，1 失败
     */
    @Column(name = "login_status")
    private Integer loginStatus;

    /**
     * 登录ip
     */
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 登录时间
     */
    @Column(name = "login_time")
    private Integer loginTime;

    /**
     * 登出时间
     */
    @Column(name = "logout_time")
    private Integer logoutTime;

    /**
     * 设备型号
     */
    private String plat;

    /**
     * 手机厂商
     */
    private String brand;

    /**
     * 手机型号/设备型号
     */
    private String mid;

    /**
     * 手机系统版本号
     */
    private String os;

    /**
     * 宽
     */
    private String w;

    /**
     * 高
     */
    private String h;

    /**
     * 手机串号
     */
    private String imei;

    /**
     * 登录来源 1-android，2-ios，3-pc，4-h5
     */
    @Column(name = "login_source")
    private String loginSource;

    /**
     * 登录参数
     */
    @Column(name = "login_params")
    private String loginParams;
    
    @Column(name = "login_result")
    private String loginResult;

    @Column(name = "device_channel")
    private String deviceChannel;

    /**
     * 定位经度
     */
    @Column(name = "lon")
    private Double lon;

    /**
     * 定位纬度
     */
    @Column(name = "lat")
    private Double lat;

    /**
     * 定位城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 定位省
     */
    @Column(name = "province")
    private String province;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}

	public String getDeviceChannel() {
		return deviceChannel;
	}

	public void setDeviceChannel(String deviceChannel) {
		this.deviceChannel = deviceChannel;
	}

	/**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取登录类型 0 mobile，1 wx，2 qq 
     *
     * @return login_type - 登录类型 0 mobile，1 wx，2 qq 
     */
    public Integer getLoginType() {
        return loginType;
    }

    /**
     * 设置登录类型 0 mobile，1 wx，2 qq 
     *
     * @param loginType 登录类型 0 mobile，1 wx，2 qq 
     */
    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    /**
     * 获取登录结果 0 成功，1 失败
     *
     * @return login_status - 登录结果 0 成功，1 失败
     */
    public Integer getLoginStatus() {
        return loginStatus;
    }

    /**
     * 设置登录结果 0 成功，1 失败
     *
     * @param loginStatus 登录结果 0 成功，1 失败
     */
    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    /**
     * 获取登录ip
     *
     * @return login_ip - 登录ip
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置登录ip
     *
     * @param loginIp 登录ip
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 获取登录时间
     *
     * @return login_time - 登录时间
     */
    public Integer getLoginTime() {
        return loginTime;
    }

    /**
     * 设置登录时间
     *
     * @param loginTime 登录时间
     */
    public void setLoginTime(Integer loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * 获取登出时间
     *
     * @return logout_time - 登出时间
     */
    public Integer getLogoutTime() {
        return logoutTime;
    }

    /**
     * 设置登出时间
     *
     * @param logoutTime 登出时间
     */
    public void setLogoutTime(Integer logoutTime) {
        this.logoutTime = logoutTime;
    }

    /**
     * 获取设备型号
     *
     * @return plat - 设备型号
     */
    public String getPlat() {
        return plat;
    }

    /**
     * 设置设备型号
     *
     * @param plat 设备型号
     */
    public void setPlat(String plat) {
        this.plat = plat;
    }

    /**
     * 获取手机厂商
     *
     * @return brand - 手机厂商
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 设置手机厂商
     *
     * @param brand 手机厂商
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 获取手机型号/设备型号
     *
     * @return mid - 手机型号/设备型号
     */
    public String getMid() {
        return mid;
    }

    /**
     * 设置手机型号/设备型号
     *
     * @param mid 手机型号/设备型号
     */
    public void setMid(String mid) {
        this.mid = mid;
    }

    /**
     * 获取手机系统版本号
     *
     * @return os - 手机系统版本号
     */
    public String getOs() {
        return os;
    }

    /**
     * 设置手机系统版本号
     *
     * @param os 手机系统版本号
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * 获取宽
     *
     * @return w - 宽
     */
    public String getW() {
        return w;
    }

    /**
     * 设置宽
     *
     * @param w 宽
     */
    public void setW(String w) {
        this.w = w;
    }

    /**
     * 获取高
     *
     * @return h - 高
     */
    public String getH() {
        return h;
    }

    /**
     * 设置高
     *
     * @param h 高
     */
    public void setH(String h) {
        this.h = h;
    }

    /**
     * 获取手机串号
     *
     * @return imei - 手机串号
     */
    public String getImei() {
        return imei;
    }

    /**
     * 设置手机串号
     *
     * @param imei 手机串号
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * 获取登录来源 1-android，2-ios，3-pc，4-h5
     *
     * @return login_source - 登录来源 1-android，2-ios，3-pc，4-h5
     */
    public String getLoginSource() {
        return loginSource;
    }

    /**
     * 设置登录来源 1-android，2-ios，3-pc，4-h5
     *
     * @param loginSource 登录来源 1-android，2-ios，3-pc，4-h5
     */
    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }

    /**
     * 获取登录参数
     *
     * @return login_params - 登录参数
     */
    public String getLoginParams() {
        return loginParams;
    }

    /**
     * 设置登录参数
     *
     * @param loginParams 登录参数
     */
    public void setLoginParams(String loginParams) {
        this.loginParams = loginParams;
    }
}