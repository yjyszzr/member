package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("代金券用户参数列表")
@Data
public class DlCashCouponUserParam {
	@ApiModelProperty("用户Id")
	private Integer userId;

}
