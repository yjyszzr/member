package com.dl.member.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DateStrParam {
    @ApiModelProperty(value = "当前日期字符串：格式2018-03-05")
    private String dateStr;
}
