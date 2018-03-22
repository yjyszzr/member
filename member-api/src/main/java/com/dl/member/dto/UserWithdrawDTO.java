package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现单号信息
 * @author zhangzirong
 *
 */
@ApiModel("提现单号信息")
@Data
public class UserWithdrawDTO {

    /**
     * 提现单号
     */
	@ApiModelProperty("提现单号")
    private String withdrawalSn;

}