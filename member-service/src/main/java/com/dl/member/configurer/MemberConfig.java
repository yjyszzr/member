package com.dl.member.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
@Data
@Configuration
public class MemberConfig {

	//juhe 实名认证
	@Value("${juhe.realname.api.url}")
	private String ralNameApiURL;
	
	@Value("${juhe.realname.key}")
	private String realNameKey;
	
	//juhe 银行卡三元素认证
	@Value("${juhe.bankauth3.api.url}")
	private String bankAuth3ApiUrl;
	
	@Value("${juhe.bankauth3.key}")
	private String bankAuth3Key;
	
	@Value("${juhe.bankauth3.sign}")
	private String bankAuth3Sign;

	//juhe 查询银行卡类型
	@Value("${juhe.bankType.api.url}")
	private String bankTypeUrl;
	
	@Value("${juhe.bankType.key}")
	private String bankTypeKey;
		
	//juhe 查询银行卡种类
	@Value("${juhe.detectBankType.api.url}")
	private String detectBankTypeUrl;
	
	@Value("${juhe.detectBankType.key}")
	private String detectBankTypeKey;
	
	//juhe 短信接口
	@Value("${juhe.sms.api.url}")
	private String juheSmsApiUrl;
	
	@Value("${juhe.sms.key}")
	private String juheSmsKey;
	
	//阿里大于 短信接口		
	@Value("${alidayu.sms.accessKeyId}")
	private String aliSmsAccessKeyId;
	
	@Value("${alidayu.sms.accessKeySecret}")
	private String aliSmsAccessKeySecret;
		
}
