package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("账户类型信息")
@Data
public class AmountTypeParam extends PageParam{

	@ApiModelProperty("账户类型：0-全部 1-奖金 2-充值 3-购彩 4-提现 5-红包")
    private String amountType;
	
}