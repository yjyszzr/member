package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlWinningLogDTO {

	@ApiModelProperty(value = "中奖信息")
	public String winningMsg;

	@ApiModelProperty(value = "中奖金额")
	public String winningMoney;
}