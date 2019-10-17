package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserNoticeDTO {

	@ApiModelProperty("卡券提醒,0没有，其它有")
	private int bonusNotice;
	@ApiModelProperty("消息提醒,0没有，其它有")
	private int messageNotice;
}
