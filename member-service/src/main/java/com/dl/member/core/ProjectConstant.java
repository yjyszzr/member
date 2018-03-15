package com.dl.member.core;

public class ProjectConstant {

	public static final String BASE_PACKAGE = "com.dl.member";
	
	public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".mapper.Mapper";
    public static final String MAPPER_BASE = "com.dl.base.mapper.Mapper";
    
    public static final String REGISTER_CAPTCHA_ = "register_captcha_";
    public static final String SMS_PREFIX = "sms_";
    public static final String REGISTER_CAPTCHA = "register_captcha";
    
    public static final String REGISTER_TPLID = "66686";
    public static final String LOGIN_TPLID = "66839";
    public static final String RESETPASS_TPLID = "66838";
	/**
	 * 缓存存放验证码的有效时长
	 */
	public final static int SMS_REDIS_EXPIRED = 48000;
    
    public static final String USER_DEFAULT_HEADING_IMG = "http://i9-static.jjwxc.net/novelimage.php?novelid=3385656&coverid=100&ver=d8d2de8a8fb398618c161418abc58f04";
    public static final String LOGIN_SOURCE_ANDROID = "1";
    public static final String LOGIN_SOURCE_IOS = "2";
    public static final String LOGIN_SOURCE_PC = "3";
    public static final String LOGIN_SOURCE_H5 = "4"; 
    
    public static final String ANDROID = "android";
    public static final String IOS = "ios";
    public static final String PC = "pc";
    public static final String H5 = "h5";
    
    public static final int USER_STATUS_NOMAL = 0;
    public static final int USER_STATUS_LOCK = 1;
    public static final int USER_STATUS_FROZEN = 2;
}
