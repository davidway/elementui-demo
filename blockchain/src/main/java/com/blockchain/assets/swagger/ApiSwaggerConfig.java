	package com.blockchain.assets.swagger;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;








import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.models.dto.ModelRef;
import com.mangofactory.swagger.models.dto.ResponseMessage;
import com.mangofactory.swagger.models.dto.builder.ResponseMessageBuilder;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableSwagger
@EnableWebMvc
@ComponentScan({"com.blockchain.controller","com.blockchain.DTO"}) 
public class ApiSwaggerConfig {	
	private SpringSwaggerConfig springSwaggerConfig;

	/*
	 * //** Required to autowire SpringSwaggerConfig
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/*
	 * //** Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	 * framework - allowing for multiple swagger groups i.e. same code base
	 * multiple swagger resource listings.
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		
		
		    
		if (SwaggerProperties.isOpen.equals(SwaggerProperties.OPEN)) {
			return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).enable(true);
		}else{
			return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).enable(false);
		}
		

	}
	

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("后台接口文档", // 页面的标题
				"0.01", "My Apps API terms of service", "zhuweiqiang@mail.weisanyun.com", "My Apps API Licence Type", "My Apps API License URL");
		return apiInfo;
	}
}