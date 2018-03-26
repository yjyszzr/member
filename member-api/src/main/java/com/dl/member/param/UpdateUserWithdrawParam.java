package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

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
	@NotBlank(message="提现单号不能为空")
    private String withdrawalSn;

	@ApiModelProperty("提现单状态：0-未完成提现 1-已完成提现 2-失败")
	@NotBlank(message="提现单状态不能为空")
    private String status;

	@ApiModelProperty("付款时间:时间戳")
	@NotBlank(message="付款时间不能为空")
    private Integer payTime;
	
	@ApiModelProperty("交易号")
	@NotBlank(message="交易号不能为空")
    private String paymentId;
	
	@ApiModelProperty("交易方名称:银行名称或第三方交易名称")
	@NotBlank(message="交易方名称不能为空")
    private String paymentName;
 
}