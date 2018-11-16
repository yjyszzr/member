package com.dl.member.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("业务配置信息")
@Data
public class SysConfigDTO {

	@ApiModelProperty("业务id")
    private Integer businessId;
	
	@ApiModelProperty("业务值")
    private BigDecimal value;
	
	@ApiModelProperty("业务描述")
    private String describtion;

	@ApiModelProperty("文本信息")
	private String valueTxt;
	
}