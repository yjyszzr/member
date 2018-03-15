package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手机号信息
 * @author zhangzirong
 *
 */
@ApiModel("手机号信息")
@Data
public class MobileDTO {

	@ApiModelProperty("手机号")
    private String mobile;
	@ApiModelProperty("供应商")
	private String supplier;
	@ApiModelProperty("省")
	private String province;	
	@ApiModelProperty("城市")
	private String city;	
}
