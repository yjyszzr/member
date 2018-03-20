package com.dl.member.enums;

public enum MemberEnums {
    ALREADY_REGISTER(301010,"该手机号已经注册，请直接登录"),
    WRONG_IDENTITY(301011,"用户名或密码错误"),
    PASS_WRONG_BEYOND_5(301012,"您的密码错误次数过多，账号已经被锁定，请过1分钟之后再试！"),
	USER_ACCOUNT_FROZEN(301013,"您的账号已经被冻结，请联系客服"),
	NO_REGISTER(301014,"该手机号还未注册，请先注册"),
	VERIFY_BANKCARD_EROOR(301015,"银行卡验证失败"),
	SMSCODE_WRONG(301016,"请输入正确的验证码"),
	BONUS_UNEXITS(301017,"红包不存在"),
	PARAMS_NOT_NULL(301018,"参数不能为空"),
	BONUS_USED(301019,"红包已使用"),
	BONUS_EXPIRE(301020,"红包已过期"),
	BONUS_UNUSED(301021,"红包未使用"),
	MONEY_IS_NOT_ENOUGH(301022,"用户余额不足"),
	USERACCOUNTS_ALREADY_REDUCE(301023,"用户账户已扣款"),
	BANKCARD_ALREADY_AUTH(301024,"该银行卡号已经认证过"),
	NOT_DEBIT_CARD(301025,"不支持添加非储蓄卡"),
	NOT_REAL_AUTH(301026,"未进行实名认证");
//	MOBILE_ERROR(301012,"手机号码格式错误"),
//	ALREADY_SMS_VERIFY(301013,"手机号已经验证过"),	
//	NO_SHOP_MEM(301014,"用户不是店铺会员"),
//	NO_SHOP_RANK(301015,"用户不享受折扣"),
//	BONUS_USED(301016,"红包已使用"),
//	BONUS_EXPIRE(301017,"红包已过期"),
//	BONUS_UNUSED(301018,"红包未使用"),
//	BONUS_UNEXITS(301019,"红包不存在"),
//	BONUS_ALREADYHAVE(301020,"红包已经领取过"),
//	ORDER_SN_NULL(301021,"未查到该订单使用红包"),
//	USER_ID_NULL(301022,"用户Id为空"),
//	USERACCOUNTS_ALREADY_REDUCE(301023,"用户账户已扣款"),
//	USERACCOUNTS_UNCHANGE(301024,"用户账户未扣款"),
//	USERPAYPOINTS_UNCHANGE(301025,"用户账户积分未曾改动"),
//	USERPAYPOINTS_ALREADY_REDUCE(301026,"用户账户积分已扣除"),
//	USERPAYPOINTS_ALREADY_ADD(301027,"用户账户积分已增加"),
//	USER_IS_NULL(301028,"未查到用户"),
//	PARAMS_NOT_NULL(301029,"参数不能为空"),
//	PAYSURPLUSPASSWORD_IS_NULL(301030,"未设置余额支付密码，请先设置余额支付密码"),
//	PAYSURPLUSPASSWORD_IS_WRONG(301031,"您输入的支付密码不正确，请重新输入"),
//	PUSH_TOKEN_IS_NULL(301032,"消息推送的token为空"),
//	DBDATA_IS_NULL(301033,"数据库无该数据"),
//	DBDATA_NOT_UNIQUE(301034,"数据库数据不唯一"),
//	DBDATA_HAS_MANY(301035,"数据有多条"),
//	SMSCODE_WRONG(301036,"请输入正确的验证码"),
//	SMSCODE_NOT_EXITS(301037,"验证码不存在"),
//	UNREGISTER(301038,"手机号未注册过，请确认后重试"),
//	MONEY_IS_NOT_ENOUGH(301039,"用户余额不足"),
//	COMMINT_BONUS_REPEAT(301040,"有重复的红包，无法进行校验红包有效性");
	
	
	
	
	
	private Integer code;
    private String msg;

    private MemberEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
