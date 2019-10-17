package com.dl.member.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserMatchCollectParam {    
    @ApiModelProperty(value = "比赛id")
    private Integer matchId;
    
    @ApiModelProperty(value = "收藏日期:2018-07-18 这样的格式")
    private String dateStr;
}
