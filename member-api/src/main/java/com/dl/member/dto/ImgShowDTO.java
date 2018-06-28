package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("头像展示")
@Data
public class ImgShowDTO {
	@ApiModelProperty("展示头像作用")
	private String imgShowUrl;
	@ApiModelProperty("保存完返回的头像信息")
	private String imgUrl;
}
