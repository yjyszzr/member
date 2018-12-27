package com.dl.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("业务配置id集合信息")
@Data
public class BusiIdsListParam {

    @ApiModelProperty("业务id集合")
    @NotNull
    private List<Integer> businessIdList;

}
