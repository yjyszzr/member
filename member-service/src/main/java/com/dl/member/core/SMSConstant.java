package com.dl.member.core;

/**
 * 项目常量
 */
public final class SMSConstant {
	/**
	 * 缓存中的库
	 */
	public final static int SMS_REDIS_DB = 6;
	/**
	 * 缓存存放验证码的有效时长
	 */
	public final static int SMS_REDIS_EXPIRED = 60;
	/**
	 * 
	 */
	public final static String SMS_MOBILE_PREFIX = "sms_";
	
	public final static String PROD_SMS_MOBILE_PREFIX = "prod_sms_";
	/**
	 * 短信模板对象缓存前缀
	 */
	public final static String SMS_TEMPLATE_PREFIX = "sms_templatee_";
	
    //短信验证码类型
    /**
     * 会员注册
     */
    public static final String REGISTER = "register_captcha";

    /**
     * 语音验证码
     */
    public static final String CALL_REGISTER = "login_register_code";

    /**
     * 动态密码登录
     */
    public static final String SMS_LOGIN = "login_captcha";

    /**
     * 忘记密码身份验证
     */
    public static final String FIND_PWD = "find_pwd_captcha";

    /**
     * 账户安全身份验证
     */
    public static final String SECURITY = "security_captcha";

    /**
     * 提现身份验证
     */
    public static final String DEPOSIT_ACCOUNT = "deposit_captcha";

    /*
     * honglian95 短信接口返回码
     */
    //'00' => '发送成功',
    //'1' => '参数不完整，请检查所带的参数名是否都正确',
    //'2' => '鉴权失败，一般是用户名或密码不正确',
    //'3' => '号码数量超过50条',
    //'4' => '发送失败',
    //'5' => '余额不足',
    //'6' => '发送内容含屏蔽词',
    //'7' => '短信内容超出350字',
    //'72' => '内容被审核员屏蔽',
    //'8' => 'null',
    //'9' => '夜间管理，不允许一次提交超过 20 个号码',
    //'10' => '{txt}不应当出现在提交数据中，请修改[模板类账号](适用于 模板类帐户)',
    //'11' => '模板匹配成功',
    //'12' => '未匹配到合适模板，已提交至审核员审核',
    //'13' => '未匹配到合适模板，无法下发，请修改',
    //'14' => '该账户没有模板',
    //'15' => '操作失败',
    //'02' => '手机号码为黑名单',
    //'81' => '手机号码错误，请检查手机号是否正确',
    //'ERR IP' => 'IP 验证未通过，请联系管理员增加鉴权 IP
    
    
    
}
