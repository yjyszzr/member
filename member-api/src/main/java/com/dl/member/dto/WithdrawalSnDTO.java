package com.dl.member.dto;

import java.math.BigDecimal;

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
public class WithdrawalSnDTO {
    /**
     * 提现单号
     */
	@ApiModelProperty("提现单号")
    private String withdrawalSn;
	
	@ApiModelProperty("提现单生成时间")
    private Integer addTime;
	
}
