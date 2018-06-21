package com.dl.member.param;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemWithDrawSnParam {
	@ApiModelProperty("提现单号")
	@NotNull
	private String withDrawSn;
	
	@ApiModelProperty("用户id")
	private Integer userId;
}
