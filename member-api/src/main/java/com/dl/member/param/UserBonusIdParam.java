package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@ApiModel("用户红包Id参数")
@Data
public class UserBonusIdParam {

	@ApiModelProperty("用户红包Id")
	@NotEmpty(message="用户红包Id")
    private Integer  userBonusId;

}
