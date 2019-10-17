package com.dl.member.param;


import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动充值钱数参数")
@Data
public class PayLogIdParam {
	
    @ApiModelProperty(value = "支付成功后的id")
    private String payLogId;
    
    @ApiModelProperty(value = "支付金额")
    private BigDecimal orderAmount;
    
    @ApiModelProperty(value = "支付用户id")
    private Integer userId;
    
    @ApiModelProperty(value = "充值流水号")
    private String accountSn;

}
