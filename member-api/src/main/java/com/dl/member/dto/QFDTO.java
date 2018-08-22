package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 活动资格返回信息
 * @author zhangzirong
 *
 */
@ApiModel("活动资格返回信息")
@Data
public class QFDTO {

	@ApiModelProperty("是否有资格:0-代表无资格 1-代表有资格")
    private Integer qfRst;
}
