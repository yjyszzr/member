package com.dl.member.dto;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAccountListAndCountDTO {
	@ApiModelProperty("账户明细集合")
	private PageInfo<UserAccountDTO> pageInfo;

	@ApiModelProperty("账户统计数据")
	private UserAccountByTimeDTO userAccountByTimeDTO;
}
