package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 某天收藏的比赛信息
 * @author zhangzirong
 *
 */
@ApiModel("真实用户信息")
@Data
public class MatchCollectSomedayCountDTO {
	@ApiModelProperty("某天收藏的比赛数量")
    private String matchCollectCount = "0";
}
