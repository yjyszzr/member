package com.dl.member.enums;

public enum MemberEnums {
	ALREADY_REGISTER(301010, "该手机号已经注册，请直接登录"), WRONG_IDENTITY(301011, "用户名或密码错误"), PASS_WRONG_BEYOND_5(301012, "密码多次错误，账号已冻结，请1分钟后再次尝试登录"), USER_ACCOUNT_FROZEN(301013, "您的账号已经被冻结，请联系客服"), NO_REGISTER(301014, "该手机号还未注册，请先注册"), VERIFY_BANKCARD_EROOR(301015, "银行卡验证失败"), SMSCODE_WRONG(301016, "请输入正确的验证码"), BONUS_UNEXITS(301017, "红包不存在"), PARAMS_NOT_NULL(301018, "参数不能为空"), BONUS_USED(301019, "红包已使用"), BONUS_EXPIRE(301020, "红包已过期"), BONUS_UNUSED(301021, "红包未使用"), MONEY_IS_NOT_ENOUGH(301022, "用户余额不足"), USERACCOUNTS_ALREADY_REDUCE(301023, "用户账户已扣款"), BANKCARD_ALREADY_AUTH(301024, "该银行卡号已经认证过"), NOT_DEBIT_CARD(301025, "不支持添加非储蓄卡"), NOT_REAL_AUTH(301026, "身份认证失败，请重试"), CURRENT_ONE_CARD(301027, "当前仅一张银行卡不能切换默认状态"), NO_BANKCARDS(301029, "当前用户下没有银行卡"), DBDATA_IS_NULL(301030, "数据库无该数据"),
	ORDER_PARAM_NOT_MATCH(301031, "与订单金额不符合"), USERREAL_ALREADY_AUTH(301032, "用户已经认证过"), PARAM_WRONG(301033, "参数错误"), DATA_ALREADY_EXIT_IN_DB(301034, "数据已存在数据库中"), BANKCARD_NOT_MATCH(301035, "用户信息与银行卡不匹配"), ARTICLE_NOT_COLLECT(301036, "用户没有收藏该文章"), BANKCARD_FORMAT_ERROR(301037, "银行卡号格式不正确"), PASS_FORMAT_ERROR(301038, "请输入6-20数字和字母组合的密码"), MOBILE_VALID_ERROR(301039, "请输入合法的手机号"), VERIFY_IDCARD_EROOR(301040, "校验身份证失败"), MONEY_PAID_NOTLESS_ZERO(301041, "扣减余额不能为负数和0"), SEND_SMS_ERROR(301042, "发送短信验证码失败"), COMPLAIN_CONTENT_NULL(301043, "投诉内容不能为空"), COMMON_ERROR(301044, "提示消息自定义"), MESSAGE_COUNT_ERROR(301045, "您今天已发送10条验证码，请明天再试"), USER_REAL_COUNTLIMIT(301046, "一张身份证最多绑定1个用户"), MESSAGE_SENDLOT_ERROR(301047, "验证码60秒内仅可发送一次,请稍后重试！"), USER_NOT_FOUND_ERROR(301048, "用户获取失败或不存在！"),
	NO_OLD_LOGIN_PASS_ERROR(301049, "请提供原始密码！"), ERR_OLD_LOGIN_PASS_ERROR(301050, "原始密码输入有误！"), ACTIVITY_NOT_VALID(301051, "活动已过期"), RECHARGE_ACT_MIN_LIMIT(301052, "不符合充值赠送的最低充值金额"), MESSAGE_10MIN_COUNT_ERROR(301053, "10分钟内不能发送超过3条，10分钟后重试"), MESSAGE_60MIN_COUNT_ERROR(301054, "60分钟内不能发送超过4条，60分钟后重试"), REAL_IDCARDNO_NOT18(301055, "很抱歉，根据法律规定未满18周岁的禁止购彩"), REAL_IDCARDNO_EMPTY(301056, "请填写身份证号"), REAL_IDCARDNO_NOTLEGAL(301057, "请输入正确的身份证号"), USER_BANK_NOT_SURPPORT(301058, "很抱歉,暂不支持此银行绑定"), USER_BANK_ADDING(301059, "银行卡添加中"), NO_UPDATE(301060, "无最新版本"), OLD_PWD_WRONG(301061, "旧密码不匹配"), SAME_MOBILE_WRONG(301062, "绑定的手机号应为同一个"), NOT_VALID_PAY(301063, "请勿多次支付"), MESSAGE_COUNT_FUND_ERROR(301064, "您今天已发送预警，请明天再试"), MOBILE_MISMATCH_WRONG(301065, "手机号与大转盘获奖手机号不符合");

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
