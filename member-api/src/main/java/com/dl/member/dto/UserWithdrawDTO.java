package com.dl.member.dto;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现单号信息
 * @author zhangzirong
 *
 */
@ApiModel("提现单号信息")
@Data
public class UserWithdrawDTO {

	
	@ApiModelProperty("提现单id")
    private Integer id;

    @ApiModelProperty("提现单号")
    private String withdrawalSn;
    
    @ApiModelProperty("提现金额")
    private BigDecimal amount;

    @ApiModelProperty("账户流水id")
    private Integer accountId;

    @ApiModelProperty("添加时间")
    private Integer addTime;

    @ApiModelProperty("提现状态,1-已完成，0-未完成")
    private String status;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("付款时间")
    private Integer payTime;    

    @ApiModelProperty("银行卡名称")
    private String bankName;
    

}