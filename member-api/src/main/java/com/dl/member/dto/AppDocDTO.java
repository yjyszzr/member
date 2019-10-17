package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppDocDTO {
	
	@ApiModelProperty(value="分类")
	private String classfify = "";
	@ApiModelProperty(value="内容")
	private String content = "";

}
