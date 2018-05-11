package com.dl.member.param;

import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户id集合
 *
 * @author zhangzirong
 */
@Data
public class UserIdListParam {
	
    @ApiModelProperty(value = "用户id集合")
    @NotEmpty
    private List<Integer> userIdList;
    
}
