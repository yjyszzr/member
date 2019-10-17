package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IDFA保存或回调
 *
 * @author zhangzirong
 */
@Data
public class IDFACallBackParam {
    @ApiModelProperty(value = "userid")
    private Integer userid;
    @ApiModelProperty(value = "设备IDFA号，包含-")
    private String idfa;
}
