package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 用户登录信息
 *
 * @author tangyongchun
 */
@Data
public class UserLoginInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请填写手机号")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @NotBlank(message = "请填写密码")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty("图片类型验证码")
    private String authCode;

}
