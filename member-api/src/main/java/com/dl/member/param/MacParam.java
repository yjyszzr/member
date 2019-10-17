package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class MacParam {

    @ApiModelProperty(value = "mac")
    @NotBlank(message = "mac不能为空")
    private String mac;
    
    @ApiModelProperty(value = "busiType")
    @NotBlank(message = "busiType不能为空")
    private Integer busiType;
}
