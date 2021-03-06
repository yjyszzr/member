package com.dl.member.dto;


import javax.persistence.Column;

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
	
	@ApiModelProperty("卡类型:1-储蓄卡")
    private String cardType;
	
	@ApiModelProperty("银行卡号后4位")
    private String lastCardNo4;
	
	@ApiModelProperty("银行英文简写")
    private String abbreviation;

	@ApiModelProperty("银行卡类型")
	private Integer type;
	
	@ApiModelProperty("银行卡用途")
	private Integer purpose;
	
	@ApiModelProperty("密文")
	private String password;
	@ApiModelProperty("是否签约")
    private Integer isSign;
}
