package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("代金券参数列表")
@Data
public class DlCashCouponParam {
	@ApiModelProperty("代金券Id")
	private Integer cashCouponId;

}
