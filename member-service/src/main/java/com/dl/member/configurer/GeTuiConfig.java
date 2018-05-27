package com.dl.member.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class GeTuiConfig {

	@Value("${gettui-AppID}")
	private String appId;
	@Value("${gettui-AppSecret}")
	private String appSecret;
	@Value("${gettui-AppKey}")
	private String appkey;
	@Value("${gettui-MasterSecret}")
	private String masterSecret;
}
