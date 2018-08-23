package com.dl.member.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("app升级DTO")
@Data
public class UpdateAppDTO {
	
	@ApiModelProperty("是否强制升级:0-非强制 1-强制")
    private String type;
	
//	@ApiModelProperty("appName:android每个app的名称编码：彩小秘,必中彩,足球咨询,天天体育,天空体育,渠道推广分别为1,2,3,4,5,6")
//    private String appNameCode;
//	
	@ApiModelProperty("渠道")
    private String channel;	
	
	@ApiModelProperty("版本")
    private String version;
	
	@ApiModelProperty("下载url")
    private String url;
	
	@ApiModelProperty("升级日志集合")
    private List<String> updateLogList;
	
}
