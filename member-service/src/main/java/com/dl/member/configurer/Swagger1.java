package com.dl.member.configurer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger1 {

	 @Bean
	    public Docket createRestApi() {
	        List<Parameter> pars = new ArrayList<>();
	        ParameterBuilder tokenPar = new ParameterBuilder();
	        tokenPar.name("token")
	                .description("令牌")
	                .modelRef(new ModelRef("string"))
	                .parameterType("header")
	                .required(false);
	        pars.add(tokenPar.build());

	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.dl.member"))
	                .paths(PathSelectors.any())
	                .build().globalOperationParameters(pars);
	    }

	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("member")
	                .description("member")
	                .termsOfServiceUrl("")
	                .version("1.0")
	                .build();
	    }
}
