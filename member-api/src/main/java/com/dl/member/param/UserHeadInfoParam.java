package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserHeadInfoParam {
	@ApiModelProperty(value = "头像")
	String headimg;
	@ApiModelProperty(value = "昵称")
	String nickname;
}
