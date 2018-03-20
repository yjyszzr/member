package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户提现参数类
 * @author zhangzirong
 *
 */
@ApiModel("用户提现参数类")
@Data
public class UpdateUserWithdrawParam {
	
	
	@ApiModelProperty("提现单号")
    private String withdrawalSn;

	@ApiModelProperty("提现单状态：0-未完成提现 1-已完成提现")
    private String status;

	@ApiModelProperty("付款时间")
    private Integer payTime;
	
	@ApiModelProperty("交易号")
    private String paymentId;
 
}