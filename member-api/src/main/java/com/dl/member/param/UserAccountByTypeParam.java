package com.dl.member.param;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAccountByTypeParam {
	
	@ApiModelProperty("变动的金额:只能为正数")
	@DecimalMin(value="0")
	private BigDecimal inOrOutMoney;
	
	@ApiModelProperty("账户变动类型:1-奖金 2-充值  4-提现")
	private Integer type;

}
