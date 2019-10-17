package com.dl.member.param;

import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;
import com.dl.member.dto.UserIdAndRewardDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserIdAndRewardListParam {
	
	@ApiModelProperty("用户id和中奖金额集合")
	@NotEmpty(message = "参数不能为空")
	private List<UserIdAndRewardDTO> userIdAndRewardList;

}
