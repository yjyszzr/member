package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateUnReadNoticeParam {

	@ApiModelProperty("类型：1我的卡券，2消息中心")
	private int type;
}
