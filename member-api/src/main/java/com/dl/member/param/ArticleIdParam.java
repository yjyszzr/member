package com.dl.member.param;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleIdParam {
	
	@ApiModelProperty(value="文章id")
	@NotNull
	private Integer articleId;

}
