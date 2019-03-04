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
    
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    
    @ApiModelProperty(value = "流水号")
    private String accountSn;
    
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
    
    @ApiModelProperty(value = "状态：0-未完成 1-已完成 2-失败")
    private Integer status;
    
    @ApiModelProperty(value = "最后更新时间")
    private Integer lastTime;
    
    @ApiModelProperty(value = "使用的红包的金额")
    private BigDecimal bonusPrice;
    
    @ApiModelProperty(value = "入库时间")
    private Integer addTime; 
    
    @ApiModelProperty(value = "processType")
    private Integer processType; 
    
    
    
}
