package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SetLoginPassParam {

	@ApiModelProperty(value = "用户登录密码")
    @NotBlank(message = "请填写用户登录密码")
    private String userLoginPass;
    
    @ApiModelProperty(value = "原登录密码")
    private String oldLoginPass;
    
    @ApiModelProperty(value = "1修改密码0设置密码")
    private int type;
}
