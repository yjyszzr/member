package com.dl.member.param;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DocClassifyParam {

    @ApiModelProperty(value = "文案分类：1-提现说明文案")
    @NotEmpty(message = "文案分类不能为空")
    private String docClassify;
	
}
