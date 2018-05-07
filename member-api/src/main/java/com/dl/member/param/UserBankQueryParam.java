package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户银行卡参数
 *
 * @author zhangzirong
 */
@Data
public class UserBankQueryParam {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private Integer userId;
    
    @ApiModelProperty(value = "银行卡号")
    @NotBlank(message = "银行卡号不能为空")
    private String bankCardCode;
}
