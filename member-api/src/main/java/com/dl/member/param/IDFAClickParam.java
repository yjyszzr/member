package com.dl.member.param;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IDFA查重参数
 *
 * @author zhangzirong
 */
@Data
public class IDFAClickParam {
    @ApiModelProperty(value = "iTunes中的ID")
    private Integer appid;
    @ApiModelProperty(value = "区分不同渠道")
    private String source;
    @ApiModelProperty(value = "设备IDFA号，包含-")
    private String idfa;
    @ApiModelProperty(value = "回调地址")
    private String callback;
    
}
