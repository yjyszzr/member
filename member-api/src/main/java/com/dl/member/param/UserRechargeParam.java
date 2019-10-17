package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRechargeParam {
	@ApiModelProperty(value = "充值单id")
    private Integer id;

    @ApiModelProperty(value = "真实姓名")
    private String rechargeSn;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
}