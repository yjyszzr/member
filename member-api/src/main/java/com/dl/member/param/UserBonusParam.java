package com.dl.member.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBonusParam implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("订单号")
	private String orderSn;
	
	@ApiModelProperty("用户红包id")
	private Integer userBonusId;
}
