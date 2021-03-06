package com.dl.member.param;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值参数信息")
@Data
public class RecharegeParam {

	@ApiModelProperty("金额:适用于所有接口")
	@Min(value = 0)
    private BigDecimal amount;
	
	@ApiModelProperty("赠送金额")
    private String giveAmount;
	
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    
    
    @ApiModelProperty(value = "支付id")
    @NotNull(message="payId 不能为空")
    private String payId;
    
    @ApiModelProperty(value = "第三方名称")
    private String thirdPartName;
    
    @ApiModelProperty(value = "第三方支付金额")
    private BigDecimal thirdPartPaid;
    
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    
    private Integer rechargeCardId;
    
    private BigDecimal rechargeCardRealValue;
    
	
}