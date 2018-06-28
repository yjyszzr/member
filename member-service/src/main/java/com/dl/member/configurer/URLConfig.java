package com.dl.member.configurer;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class URLConfig {
	@Value("${uploadURL}")
	private String uploadURL;
	@Value("${imgShowUrl}")
	private String imgShowUrl;

}
