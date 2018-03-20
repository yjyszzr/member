package com.dl.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户提现显示界面信息
 * @author zhangzirong
 *
 */
@ApiModel("用户提现显示界面信息")
@Data
public class WithDrawShowDTO {
	@ApiModelProperty("可提现余额")
    private String userMoney;

	@ApiModelProperty("默认银行卡显示信息")
    private String defaultBankCardLabel;
	
}
