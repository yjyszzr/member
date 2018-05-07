package com.dl.member.param;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户账户流水类型参数
 *
 * @author zhangzirong
 */
@Data
public class UserAccountParamByType {
    
    @ApiModelProperty(value = "流水类型：2-充值  3-购彩  4 -提现",required = true)
    @NotNull
    private Integer accountType;
    
    @ApiModelProperty(value = "资金变化值为正数",required = true)
    @Min(value=0)
    private BigDecimal amount;       
    
    @ApiModelProperty(value = "融宝支付或微信支付名称不为空",required = true)
    @NotNull
    private String paymentName;
    
    @ApiModelProperty(value = "订单号不为空",required = true)
    @NotNull
    private String orderSn;
    
    @ApiModelProperty(value = "支付id不为空",required = true)
    @NotNull
    private Integer payId;
    
    @ApiModelProperty(value = "第三方名称不为空",required = true)
    @NotNull
    private String thirdPartName;
    
    @ApiModelProperty(value = "第三方支付金额",required = true)
    @Min(value=0)
    private BigDecimal thirdPartPaid;
    
    @ApiModelProperty(value = "红包的金额为正数",required = true)
    @Min(value=0)
    private BigDecimal bonusPrice;
    
    @ApiModelProperty(value = "用户id")
    @NotNull
    private Integer userId;

}
