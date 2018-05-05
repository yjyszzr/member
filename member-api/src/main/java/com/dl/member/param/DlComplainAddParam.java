package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlComplainAddParam {
	
    @ApiModelProperty(value = "投诉内容")
    private String complainContent;
    

}
