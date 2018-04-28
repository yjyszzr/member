package com.dl.member.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceParam {
	 @ApiModelProperty("平台：0-iphone 1-android 2-h5(小写)")
	 @NotNull(message = "平台不能为空")
	 private String plat;
	 
	 @ApiModelProperty("协议版本号，初始值为1")
	 private String apiv;
	
	 @ApiModelProperty(value = "app版本号，三位数字描述，例如：1.0.0")
	 @NotNull(message = "app版本号不能为空")
	 private String appv;
	 
	 @ApiModelProperty(value = "app唯一标识符。android取包名，IOS取值main BandleID")
	 private String appid;
	 
	 @ApiModelProperty(value = "设备网卡mac地址")
	 private String mac;
	 
	 @ApiModelProperty(value = "宽")
	 private String w;
	 
	 @ApiModelProperty(value = "高")
	 private String h;
	 
	 @ApiModelProperty(value = "系统固件版本")
	 private String os;
	 
	 @ApiModelProperty(value = "设备具体型号信息，据此判断配件类型")
	 private String mid;
	 
	 @ApiModelProperty(value = "app编译时间，精确到秒")
	 private String build;
	 
	 @ApiModelProperty(value = "手机品牌")
	 private String brand;
	 
	 @ApiModelProperty(value = "推广渠道，如(1000a)")
	 private String channel;
	 
	 @ApiModelProperty(value = "前网络连接类型:WIFI、3G、2G、mobile和UNKNOWN")
	 private String net;
	 
	 @ApiModelProperty(value = "用户账号验证串,用于验证用户id真实性")
	 private String token;
}
