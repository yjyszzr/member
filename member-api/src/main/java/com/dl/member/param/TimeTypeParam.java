package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("账户类型信息")
@Data
public class TimeTypeParam {

	@ApiModelProperty("时间段：0-全部 1-当天 2-前一周 3-前一个月 4-前三个月 ")
    private String timeType;
}
