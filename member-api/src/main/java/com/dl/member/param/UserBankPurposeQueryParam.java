package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBankPurposeQueryParam {
	@ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private Integer userId;
    
    @ApiModelProperty(value = "银行卡号")
    @NotBlank(message = "银行卡号不能为空")
    private String bankCardCode;
    
	@ApiModelProperty(value = "用途0绑卡  1支付")
    @NotBlank(message = "用途")
    private Integer purpose;
}
