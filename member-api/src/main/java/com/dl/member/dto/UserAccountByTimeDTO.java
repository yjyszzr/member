package com.dl.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户当前月统计的DTO
 * @author zhangzirong
 *
 */
@ApiModel("用户统计的DTO")
@Data
public class UserAccountByTimeDTO {
	@ApiModelProperty("提现统计")
    private String withDrawMoney;

	@ApiModelProperty("充值统计")
    private String rechargeMoney;

	@ApiModelProperty("购彩统计")
    private String buyMoney;
	
    @ApiModelProperty("奖金统计")
    private String rewardMoney;
}