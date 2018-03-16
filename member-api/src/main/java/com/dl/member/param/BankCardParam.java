package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行卡号参数
 *
 * @author zhangzirong
 */
@Data
public class BankCardParam {

    @ApiModelProperty(value = "银行卡号")
    @NotBlank(message = "银行卡号不能为空")
    private String bankCardNo;
}
