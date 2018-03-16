package com.dl.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户红包信息
 *
 * @author zhangzirong
 */
@ApiModel("用户红包信息")
@Data
public class UserBonusDTO {
    @ApiModelProperty(value = "用户红包id")
    private Integer userBonusId;
    @ApiModelProperty(value = "活动红包id")
    private Integer bonusId;
    @ApiModelProperty(value = "红包面值")
    private BigDecimal bonusPrice;
    @ApiModelProperty(value = "红包类型")
    private Integer bonusType;    
    @ApiModelProperty(value = "红包开始使用时间")
    private String startTime;
    @ApiModelProperty(value = "红包结束使用时间")
    private String endTime;
    @ApiModelProperty(value = "红包时间可使用时间段")
    private String limitTime;  
    @ApiModelProperty(value = "红包券名称")
    private String bonusName; 
    @ApiModelProperty(value = "红包使用范围")
    private String useRange; 
    @ApiModelProperty(value = "最小订单金额限制")
    private String minGoodsAmount; 
    @ApiModelProperty(value = "红包使用状态：0-未使用 1-已使用 2-已过期")
    private String bonusStatus;
	@ApiModelProperty("红包是否可以领取， 1为可以领取  0为不可以领取")
	private Integer isCanReceive;
    @ApiModelProperty(value = "限制的彩票种类")
    private List<String> lotteryId;
	
}
