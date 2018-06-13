package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户银行卡信息")
@Data
public class BankDTO {
	@ApiModelProperty("")
	private String bin_digits;
	@ApiModelProperty("金卡")
	private String nature;
	@ApiModelProperty("城市")
	private String city;
	@ApiModelProperty("英文简称")
	private String abbreviation;
	@ApiModelProperty("省份")
	private String province;
	@ApiModelProperty("位数")
	private String card_digits;
	@ApiModelProperty("官网")
	private String weburl;
	@ApiModelProperty("银行卡号")
	private String bankcard;
	@ApiModelProperty("银行名称")
	private String bankname;
	@ApiModelProperty("银行卡类型")
	private String cardtype;
	@ApiModelProperty("客服电话")
	private String kefu;
	@ApiModelProperty("")
	private boolean isLuhn;
	@ApiModelProperty("银行logo信息")
	private String banklogo;
	@ApiModelProperty("")
	private String card_bin;
}
