package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

@Data
public class UpdateImgParam {

	@NotBlank(message = "请提供图片后缀")
	@ApiModelProperty(value = "图片文件的后缀", required = true)
	private String fileType;
	@NotBlank(message = "请提供图片base64")
	@ApiModelProperty(value = "图片base64", required = true)
	private String file;
}
