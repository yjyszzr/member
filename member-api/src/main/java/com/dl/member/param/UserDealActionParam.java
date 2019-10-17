package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDealActionParam {

	@ApiModelProperty("用户id")
	private Integer userId;
}
