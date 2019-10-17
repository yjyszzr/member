package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("订单实际付款金额")
@Data
public class BonusLimitConditionParam {

	@ApiModelProperty("订单实际付款金额")
    private BigDecimal orderMoneyPaid;
	
    //可定义其他参数
	
}