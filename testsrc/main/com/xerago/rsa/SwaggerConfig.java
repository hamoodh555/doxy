package com.xerago.rsa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {

	@Value("${swagger.host}")
	private String host;
	
	@Bean
    public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
        		.select()
        		.apis(RequestHandlerSelectors.basePackage("com.xerago.rsa.web"))
        		.paths(PathSelectors.any()).build()
        		.apiInfo(apiInfo())
				.host(host);
    }
     
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Two Wheeler - MicroServices")
                .description("An API description for Two Wheeler product")
                .version("1.0")
                .build();
    }
}