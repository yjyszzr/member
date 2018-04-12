package com.dl.member.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserIdAndRewardDTO {
	
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    
    @ApiModelProperty(value = "中奖金额")
    private BigDecimal reward;
    
    @ApiModelProperty(value = "订单号")
    private String orderSn;
    
    @ApiModelProperty(value = "不需要传递：当前用户的可提现余额")
    private BigDecimal userMoney;

}
