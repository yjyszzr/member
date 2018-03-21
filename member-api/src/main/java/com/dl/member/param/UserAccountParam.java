package com.dl.member.param;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户账户流水参数
 *
 * @author zhangzirong
 */
@Data
public class UserAccountParam {
    @ApiModelProperty(value = "账户名")
    private String userName;
    
    @ApiModelProperty(value = "流水类型")
    private Integer accountType;
    
    @ApiModelProperty(value = "资金变化:注意正负")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "当前账户余额")
    private BigDecimal curBalance;
    
    @ApiModelProperty(value = "支付代码")
    private String paymentCode;
    
    @ApiModelProperty(value = "支付名称")
    private String paymentName;
    
    @ApiModelProperty(value = "订单号")
    private String orderSn;
    
    @ApiModelProperty(value = "日志")
    private String note;
    
}
