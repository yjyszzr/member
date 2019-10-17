package com.dl.member.param;


import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值单号信息")
@Data
public class UpdateUserRechargeParam {
	
	@ApiModelProperty("充值单单号")
	@NotBlank(message="充值单单号不能为空")
    private String rechargeSn;
	
	@ApiModelProperty("充值单状态：0-未完成充值 1-已完成充值  2-失败")
	@NotBlank(message="充值单状态不能为空")
    private String status;
	
    @ApiModelProperty("支付方式代码")
    @NotBlank(message="支付方式代码不能为空")
    private String paymentCode;

    @ApiModelProperty("支付方式名称")
    @NotBlank(message="支付方式名称不能为空")
    private String paymentName;

    @ApiModelProperty("付款时间：时间戳")
    @NotBlank(message="付款时间不能为空")
    private Integer payTime;

    @ApiModelProperty("交易号")
    @NotBlank(message="交易号不能为空")
    private String paymentId;

}