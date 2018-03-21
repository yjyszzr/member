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
	@ApiModelProperty("用户银行卡id")
    private String userBankId;
	
	@ApiModelProperty("可提现余额")
    private String userMoney;

	@ApiModelProperty("银行卡名")
    private String bankName;
	
	@ApiModelProperty("默认银行卡标签")
    private String defaultBankLabel;
	
}
