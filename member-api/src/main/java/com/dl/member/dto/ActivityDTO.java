package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityDTO {
	
	@ApiModelProperty(value="标题")
	private String title;
	@ApiModelProperty(value=" 描述/ 详情/ 说明")
	private String detail;
	@ApiModelProperty(value="图标")
	private String icon;
	@ApiModelProperty(value="活动跳转链接")
	private String actUrl;
	@ApiModelProperty(value="活动是否有效：0-有效 1-无效")
	private Integer isFinish;
	
}
