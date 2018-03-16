package com.dl.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户账户流水DTO
 * @author zhangzirong
 *
 */
@ApiModel("用户账户流水DTO")
@Data
public class UserAccountDTO {
	@ApiModelProperty("id")
    private Integer id;

	@ApiModelProperty("流水号")
    private String accountSn;

	@ApiModelProperty("用户id")
    private Integer userId;

	@ApiModelProperty("变化的金额")
    private String changeAmount;

    @ApiModelProperty("添加时间")
    private String addTime;
    
    @ApiModelProperty("操作类型名称")
    private String processTypeName;

    @ApiModelProperty("操作备注")
    private String note;
}