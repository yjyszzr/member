package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 真实用户参数
 *
 * @author zhangzirong
 */
@Data
public class UserRealParam {
		
    @ApiModelProperty(value = "真实姓名")
    @NotBlank(message = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    @NotBlank(message = "身份证号")
    private String IDCode;

}
