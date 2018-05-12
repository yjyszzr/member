package com.dl.member.param;

import java.math.BigDecimal;
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
public class UserWithdrawParam {

	@ApiModelProperty("提现金额")
    private BigDecimal amount;

	@ApiModelProperty("真实姓名")
    private String realName;

	@ApiModelProperty("银行卡号")
    private String cardNo;
	
	@ApiModelProperty("银行卡号")
    private String status;			//提现状态 2-失败,1-已完成，0-未完成
	
	public static final String STATUS_FAILURE = "2";
	public static final String STATUS_SUCC = "1";
	public static final String STATUS_UNCOMPLETE = "0";
}