package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行卡号参数
 *
 * @author zhangzirong
 */
@Data
public class SoUrlParam {
	@ApiModelProperty(value = "用户ID")
    private Integer userId;
	
    @ApiModelProperty(value = "原始url")
    private String link;
}
