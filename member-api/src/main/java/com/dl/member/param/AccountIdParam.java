package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("账户流水id")
@Data
public class AccountIdParam {
	
	@ApiModelProperty("账户流水id")
    private Integer accountId;
}
