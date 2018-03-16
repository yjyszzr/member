package com.dl.member.param;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 回滚余额支付参数
 * @author zhangzirong
 *
 */
@ApiModel("回滚余额支付参数")
@Data
public class RollackSurplusPayParam {

    @ApiModelProperty("扣减的账户余额")
    private BigDecimal surplus;
}
