package com.dl.member.param;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 余额支付参数
 * @author zhangzirong
 *
 */
@ApiModel("账户扣减参数")
@Data
public class SurplusPayParam {

	@ApiModelProperty("订单号")
	private String orderSn;
    @ApiModelProperty("扣减的账户余额")
    private BigDecimal surplus;
    
}
