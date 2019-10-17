package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MediaTokenParam {
	@ApiModelProperty(value = "type 0图片 1音乐 2视频")
	private int type;
}
