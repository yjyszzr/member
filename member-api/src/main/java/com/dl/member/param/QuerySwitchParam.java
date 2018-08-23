package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySwitchParam {

	@ApiModelProperty(value = "空字符串")
    private String str;
    @ApiModelProperty(value = "经度")
    private String longValue;
    @ApiModelProperty(value = "纬度")
    private String latValue;
    @ApiModelProperty(value = "省份")
    private String provinceCode;
    @ApiModelProperty(value = "城市")
    private String cityCode;
}
