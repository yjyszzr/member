package com.dl.member.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值单号信息")
@Data
public class UserRechargeDTO {

	@ApiModelProperty(" 充值单号")
    private String rechargeSn;

	@ApiModelProperty("用户ID")
    private Integer userId;

	@ApiModelProperty("充值金额")
    private BigDecimal amount;

	@ApiModelProperty("")
    private Integer accountId;

	@ApiModelProperty("添加时间")
    private Integer addTime;

	@ApiModelProperty("充值状态")
    private String status;

	@ApiModelProperty("支付代码")
    private String paymentCode;

	@ApiModelProperty("支付方式名称")
    private String paymentName;

	@ApiModelProperty("付款时间")
    private Integer payTime;

	@ApiModelProperty("")
    private String paymentId;

	@ApiModelProperty("赠送id")
    private String donationId;

}