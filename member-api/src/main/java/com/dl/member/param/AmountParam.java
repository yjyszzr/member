package com.dl.member.param;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("金额信息")
@Data
public class AmountParam {

	@ApiModelProperty("金额:适用于所有接口")
    private BigDecimal amount;
	
}