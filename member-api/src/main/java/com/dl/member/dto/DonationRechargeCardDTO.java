package com.dl.member.dto;

import java.math.BigDecimal;
import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 充值卡
 * @author zhangzirong
 *
 */
@ApiModel("充值卡信息")
@Data
public class DonationRechargeCardDTO {

	@ApiModelProperty("充值卡id")
    private String rechargeCardId;

	@ApiModelProperty("充值卡名称")
    private String name;

	@ApiModelProperty("描述：单笔充值")
    private String description;

	@ApiModelProperty("充值卡实际价值")
    private String realValue;
	
	@ApiModelProperty("赠送价值描述")
    private String donationValueDesc;

	@ApiModelProperty("充值类型:0-冲多少送多少,最高返20 1-随机送")
    private String type;
	
	@ApiModelProperty("充值类型标签:0-冲多少送多少,最高返20 1-随机送")
    private String typeLabel;

}