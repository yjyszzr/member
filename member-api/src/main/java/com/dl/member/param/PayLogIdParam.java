package com.dl.member.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动充值钱数参数")
@Data
public class PayLogIdParam {
	
    @ApiModelProperty(value = "支付成功后的id")
    private String payLogId;

}
