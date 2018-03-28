package com.dl.member.param;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 余额支付参数
 * @author zhangzirong
 *
 */
@ApiModel("账户扣减参数")
@Data
public class SurplusPayParam {

	@ApiModelProperty("订单号")
	@NotBlank(message = "订单号不能为空")
	private String orderSn;
	
    @ApiModelProperty("扣减的用户余额")
    @NotBlank(message = "扣减的用户余额不能为空")
    private BigDecimal surplus;
    
    @ApiModelProperty("实际扣减余额")
    @NotBlank(message = "实际扣减余额不能为空")
    private BigDecimal moneyPaid;
    
    @ApiModelProperty("支付方式:0-支付宝 1-微信 2-余额支付 3-混合支付")
    @NotBlank(message = "支付方式不能为空")
    private Integer payType;
    
    @ApiModelProperty("第三方支付名称")
    private String thirdPartName;
    
    @ApiModelProperty("第三方支付的钱数")
    private BigDecimal thirdPartPaid;
    
    @ApiModelProperty("红包支付的钱")
    private BigDecimal bonusMoney;
    
}
