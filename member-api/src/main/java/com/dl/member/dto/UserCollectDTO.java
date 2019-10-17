package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户收藏")
@Data
public class UserCollectDTO {
	
	@ApiModelProperty("收藏主键")
    private String id;

    @ApiModelProperty("文章id")
    private String articleId;

    @ApiModelProperty("收藏时间")
    private String addTime;

    @ApiModelProperty("收藏来源")
    private String collectFrom;
}