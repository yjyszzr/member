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
	@ApiModelProperty(value = "bucketName")
	private String fileName;

	
	public static MediaTokenDTO getEntity() {
		MediaTokenDTO entity = new MediaTokenDTO();
		entity.accKeyId = "LTAIQ2LG8kd1IU1k";
		entity.accKeySecret = "Fu8InPLKtEgKSXJ6tJztj5BKnaj6DN";
		entity.bucketName = "szcq-pic";
		entity.fileName = MD5Util.crypt(System.currentTimeMillis()+"");
		return entity;
	}
}
