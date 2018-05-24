package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlCashCouponUserParam {
	@ApiModelProperty("用户Id")
	private Integer userId;

}
