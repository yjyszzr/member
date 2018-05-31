package com.dl.member.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserIdParam {
	 @ApiModelProperty(value = "用户ID")
	 private Integer userId;
	 @ApiModelProperty(value = "是否需要真实Mobile")
	 private boolean isReal;
}
