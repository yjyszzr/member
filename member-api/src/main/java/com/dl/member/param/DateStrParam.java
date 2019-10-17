package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日期字符串参数
 *
 * @author zhangzirong
 */
@Data
public class DateStrParam {
    @ApiModelProperty(value = "日期字符串")
    private String dateStr;
}
