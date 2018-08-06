package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IDFA查重参数
 *
 * @author zhangzirong
 */
@Data
public class IDFACheckParam {
    @ApiModelProperty(value = "iTunes中的ID")
    private String appid;
    @ApiModelProperty(value = "设备IDFA号，包含-")
    private String idfa;
}
