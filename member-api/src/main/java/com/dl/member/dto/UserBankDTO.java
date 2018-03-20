package com.dl.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户银行卡信息
 * @author zhangzirong
 *
 */
@ApiModel("用户银行卡信息")
@Data
public class UserBankDTO {
	@ApiModelProperty("银行卡主键")
    private String userBankId;

	@ApiModelProperty("真实姓名")
    private String realName;

	@ApiModelProperty("银行卡号")
    private String cardNo;

	@ApiModelProperty("0-非当前默认,1-当前默认")
    private String status;

	@ApiModelProperty("银行卡logo")
    private String bankLogo;

	@ApiModelProperty("银行卡名称")
    private String bankName;
	
	@ApiModelProperty("卡类型")
    private String cardType;
	
	
}
