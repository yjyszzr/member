package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageParam {
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页展示数")
    private Integer pageSize = 15;
}
