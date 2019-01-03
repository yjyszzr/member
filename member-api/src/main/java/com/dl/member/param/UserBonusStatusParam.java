package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBonusStatusParam extends PageParam{
	
	@ApiModelProperty("红包状态:空字符串-全部   0-未使用  1-已使用 2-已过期")
	private String status;
}
