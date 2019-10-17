package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批量执行结果
 * @author 
 *
 */
@ApiModel("批量执行结果")
@Data
public class BatchResultDTO {
	
	@ApiModelProperty("批量执行结果:0 执行批量操作异常，但是回滚成功;  -1 执行批量操作异常，且回滚失败;  1批量更新成功")
    private Integer rst;
}
