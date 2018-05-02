package com.dl.member.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceKeyParam {
	
	@ApiModelProperty("设备参数")
	private DeviceParam deviceParam;

}
