package com.dl.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.dl.base.configurer.FeignConfiguration;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.configurer.WebMvcConfigurer;
import com.dl.member.configurer.Swagger1;
import com.dl.member.core.ProjectConstant;

@SpringBootApplication
@Import({RestTemplateConfig.class, Swagger1.class, WebMvcConfigurer.class, FeignConfiguration.class})
@MapperScan(ProjectConstant.MAPPER_PACKAGE)
@EnableEurekaClient
@EnableFeignClients({"com.dl.member.api","com.dl.order.api","com.dl.lottery.api","com.dl.shop.auth.api","com.dl.shop.payment.api"})
public class MemberServiceApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}
