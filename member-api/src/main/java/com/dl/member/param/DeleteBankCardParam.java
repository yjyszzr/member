package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteBankCardParam {
    @ApiModelProperty(value = "数据库表主键")
    private Integer id;
    
    @ApiModelProperty(value = "是否默认：1-默认 0-非默认")
    private String status;

}
