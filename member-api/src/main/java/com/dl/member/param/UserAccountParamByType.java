package com.dl.member.param;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户账户流水类型参数
 *
 * @author zhangzirong
 */
@Data
public class UserAccountParamByType {
    
    @ApiModelProperty(value = "流水类型")
    private Integer accountType;
    
    @ApiModelProperty(value = "资金变化:注意正负")
    private BigDecimal amount;       
    
    @ApiModelProperty(value = "支付名称")
    private String paymentName;
    
    @ApiModelProperty(value = "订单号")
    private String orderSn;
    
    @ApiModelProperty(value = "支付id")
    private String payId;
    
    @ApiModelProperty(value = "使用的可提现余额")
    private BigDecimal userSurplus;
    
    @ApiModelProperty(value = "使用的不可提现余额")
    private BigDecimal userSurplusLimit;
    
    @ApiModelProperty(value = "第三方名称")
    private String thirdPartName;
    
    @ApiModelProperty(value = "第三方支付金额")
    private BigDecimal thirdPartPaid;
    
    @ApiModelProperty(value = "状态：1-已完成 ")
    private Integer status;
    
    @ApiModelProperty(value = "使用的红包的金额")
    private BigDecimal bonusPrice;

}
