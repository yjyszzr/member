package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户信息参数类
 * @author zhangzirong
 *
 */
@ApiModel("用户信息参数类")
@Data
public class UserParam implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户Id")
	private String userId;	
	@ApiModelProperty("用户名")
	private String userName;
	@NotBlank(message="密码不能为空")
	@ApiModelProperty("密码")
	private String passWord;
	@NotBlank(message="手机号不能为空")
	@ApiModelProperty("手机号")
	private String mobile;	
	@ApiModelProperty("昵称")
	private String nickName;
	@ApiModelProperty("注册IP")
	private String regIp;
	@ApiModelProperty("上次登录IP")
	private String lastIp;
	@ApiModelProperty("登录密码盐")
	private String salt;
	@ApiModelProperty("登录类型")
	private String loginType;
    @ApiModelProperty("登录来源 ")
    private String loginSource;	
	@ApiModelProperty("可提现余额")
	private BigDecimal userMoney;	
	@ApiModelProperty("不可提现余额")
	private BigDecimal userMoneyLimit;
	@ApiModelProperty("冻结金额")
	private BigDecimal frozenMoney;	
	@ApiModelProperty("支付二维码")
	private String payQRcodeUrl; 
	@ApiModelProperty("消息推送key")
	private String pushKey;

	@ApiModelProperty("是否超级白名单")
	private String isSuperWhite;

	@ApiModelProperty("邀请码")
	private String invitCode;

}
