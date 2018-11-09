package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MobilePwdCreateParam {
	@ApiModelProperty(value = "电话")
	String mobile;
	@ApiModelProperty(value = "密码")
	String pwd;
}
