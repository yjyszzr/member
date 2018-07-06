package com.dl.member.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserMatchCollectParam {    
    @ApiModelProperty(value = "比赛id")
    private Integer matchId;
}
