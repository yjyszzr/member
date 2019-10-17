package com.dl.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户当前月统计的DTO
 * @author zhangzirong
 *
 */
@ApiModel("用户当前月统计的DTO")
@Data
public class UserAccountCurMonthDTO {
	@ApiModelProperty("当前月提现统计")
    private String withDrawMoney;

	@ApiModelProperty("当前月充值统计")
    private String rechargeMoney;

	@ApiModelProperty("当前月购彩统计")
    private String buyMoney;
	
    @ApiModelProperty("当前月奖金统计")
    private String rewardMoney;
}