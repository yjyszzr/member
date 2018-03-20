package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IDParam {
    @ApiModelProperty(value = "数据库表主键")
    private Integer id;

}
