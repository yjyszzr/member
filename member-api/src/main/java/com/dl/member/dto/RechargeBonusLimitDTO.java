package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("生成充值红包的条件")
@Data
public class RechargeBonusLimitDTO {
	
	@ApiModelProperty("红包数量")
    private Integer bonusNum;
	
	@ApiModelProperty("红包金额")
    private Double bonusPrice;
	
	@ApiModelProperty("最小订单使用金额")
    private Double minGoodsAmount;

}
