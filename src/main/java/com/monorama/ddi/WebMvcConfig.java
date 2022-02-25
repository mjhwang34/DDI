package com.monorama.ddi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        LoginInterceptor loginIntercepter = new LoginInterceptor();
        registry.addInterceptor(loginIntercepter)
        .addPathPatterns("/api/users/login")
		//"/api/dfi/foods", "/api/dfi/drugs", "/api/dfi/search", 
		//"/api/ddi/drugs", "/api/ddi/pk", "/api/ddi/protein", "api/ddi/gene", "api/ddi/substitution")
                .excludePathPatterns();
    }
}