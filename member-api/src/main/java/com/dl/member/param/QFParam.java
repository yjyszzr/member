package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("资格信息")
@Data
public class QFParam {

	@ApiModelProperty("活动类型:充值活动 -1")
    private String act_type;
	
	@ApiModelProperty("活动id:充值活动 -3")
    private String act_id;
	
	@ApiModelProperty("用户id:前端调用的时候不传")
    private String user_id;
	
}