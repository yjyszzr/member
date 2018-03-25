package com.dl.member.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值单号信息")
@Data
public class UpdateUserRechargeParam {
	
	@ApiModelProperty("充值单单号")
    private String rechargeSn;
	
	@ApiModelProperty("充值单状态：0-未完成充值 1-已完成充值")
    private String status;
	
    @ApiModelProperty("支付代码")
    private String paymentCode;

    @ApiModelProperty("支付方式名称")
    private String paymentName;

    @ApiModelProperty("付款时间")
    private Integer payTime;

    @ApiModelProperty("交易号")
    private String paymentId;

}