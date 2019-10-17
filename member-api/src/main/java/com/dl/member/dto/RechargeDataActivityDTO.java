package com.dl.member.dto;


import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值界面返回的数据")
@Data
public class RechargeDataActivityDTO {
	
	@ApiModelProperty(value="0-没有  1-有")
	private Integer isHaveRechargeAct;
	
	@ApiModelProperty(value="充值的钱(key)和最大赠送的钱(value)")
	private Map<Integer,Integer> donationPriceMap;
	
}
