package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("充值单号信息")
@Data
public class UserRechargeDTO {
    /**
     * 充值单号
     */
	@ApiModelProperty(" 充值单号")
    private String rechargeSn;

}