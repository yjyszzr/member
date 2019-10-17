package com.dl.member.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户收藏参数")
@Data
public class UserCollectParam {
	
	@ApiModelProperty("文章主键")
	@NotBlank(message = "文章主键不能为空")
    private String articleId;
	
	@ApiModelProperty("文章标题")
	@NotBlank(message = "文章标题不能为空")
    private String articleTitle;
	
	@ApiModelProperty("收藏来源:暂时传空")
    private String collectFrom;

}
