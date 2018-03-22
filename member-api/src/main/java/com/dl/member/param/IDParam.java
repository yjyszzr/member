package com.dl.member.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IDParam {
    @ApiModelProperty(value = "数据库表主键")
    @NotBlank(message = "id不能为空")
    private Integer id;

}
