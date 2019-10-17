package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("更新账户流水信息参数")
@Data
public class UpdateUserAccountParam {
	
	@ApiModelProperty("payId")
    private String payId;
	
	@ApiModelProperty("status:1-成功 2-失败")
    private Integer status;
	
	@ApiModelProperty("accountSn:账户流水号")
    private String accountSn;
}
