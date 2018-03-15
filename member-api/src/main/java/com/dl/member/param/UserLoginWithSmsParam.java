package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户第三方登录类型参数
 *
 * @author zhangzirong
 */
@Data
public class UserLoginWithSmsParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机设备信息")
    private UserDeviceParam device;

    @NotBlank(message = "请填写手机号")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

	@NotBlank(message="短信验证码不能为空")
	@ApiModelProperty(value = "短信验证码",required = true)
	String smsCode;

    @ApiModelProperty("登录来源 1 android，2 ios，3 pc，4 h5")
    private String loginSource;

//    @NotBlank(message = "请填写第三方登录类型")
//    @ApiModelProperty(value = "第三方登录类型", required = true)
//    private String thirdLoginType;
//
//    @NotBlank(message = "请填写第三方登录guid")
//    @ApiModelProperty(value = "第三方登录guid", required = true)
//    private String thirdUserGuid;
}
