package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账户流水id信息
 * @author zhangzirong
 *
 */
@ApiModel("账户流水id信息")
@Data
public class AccountIDDTO {

	@ApiModelProperty("账户流水id")
    private Integer accountId;
	
}
