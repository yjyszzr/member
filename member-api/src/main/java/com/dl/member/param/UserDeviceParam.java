package com.dl.member.param;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录设备参数
 *
 * @author zhangzirong
 */
@Data
public class UserDeviceParam implements Serializable{

	private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "登录IP")
    private String ip;

    @ApiModelProperty(value = "设备类型")
    private String plat;

    @ApiModelProperty(value = "手机厂商")
    private String brand;

    @ApiModelProperty(value = "手机型号/设备型号")
    private String mid;

    @ApiModelProperty(value = "手机系统版本号")
    private String os;

    @ApiModelProperty(value = "宽度")
    private String w;

    @ApiModelProperty(value = "高度")
    private String h;

    @ApiModelProperty(value = "手机串号")
    private String imei;

    @ApiModelProperty(value = "登录来源")
    private String loginSource;

    @ApiModelProperty(value = "登录参数")
    private String loginParams;
    
    @ApiModelProperty(value = "消息推送的token:其中android和IOS端要传这个参数（消息推送获取）,pc,h5传空字符串")
    private String pushDeviceToken;
}
