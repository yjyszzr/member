package com.dl.member.dto;

import com.dl.base.util.MD5Util;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("返回信息")
@Data
public class MediaTokenDTO {
	@ApiModelProperty(value = "accKeyId")
	private String accKeyId;
	@ApiModelProperty(value = "AccessKeySecret")
	private String accKeySecret;
	@ApiModelProperty(value = "bucketName")
	private String bucketName;
	@ApiModelProperty(value = "fileName")
	public String fileName;
	@ApiModelProperty(value="url")
	private String url;
	
	public synchronized static MediaTokenDTO getEntity(int type){
		MediaTokenDTO entity = new MediaTokenDTO();
		entity.accKeyId = "LTAIQ2LG8kd1IU1k";
		entity.accKeySecret = "Fu8InPLKtEgKSXJ6tJztj5BKnaj6DN";
		if(type == 0) {
			entity.bucketName = "szcq-pic";	
		}else if(type == 1) {
			entity.bucketName = "szcq-music";	
		}else if(type == 2) {
			entity.bucketName = "szcq-video";	
		}else {
			entity.bucketName = "szcq-pic";	
		}
		entity.fileName = MD5Util.crypt(System.currentTimeMillis()+"") + ".png";
		entity.url = "http://oss-cn-beijing.aliyuncs.com";
		return entity;
	}

	public static synchronized String buildFileName() {
		String name = MD5Util.crypt(System.currentTimeMillis()+"") + ".png";
		return name;
	}
}
