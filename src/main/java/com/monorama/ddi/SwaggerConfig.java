package com.monorama.ddi;
/*swagger접속 
 *swagger2 url: http://localhost:8080/swagger-ui.html
 *swagger3 url: http://localhost:8080/swagger-ui/index.html --> 이거 써야함*/
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

	private static final String API_NAME = "DDI Rest Api";
	private static final String API_VERSION = "1.0.0";
	private static final String API_DESCRIPTION = "DDI Rest Api 문서";
	@Bean
    public Docket restAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.monorama.ddi"))
                //.apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                //.paths(PathSelectors.ant("/v1/**"))
                .build()
                .useDefaultResponseMessages(false)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }
}