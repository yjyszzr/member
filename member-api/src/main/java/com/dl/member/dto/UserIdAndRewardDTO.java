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

}
