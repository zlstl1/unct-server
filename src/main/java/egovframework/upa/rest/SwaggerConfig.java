package egovframework.upa.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
/* @EnableSwagger2WebMvc */
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {

	@Bean
	public Docket newApiAll() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("00. All Device API REST Service")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.any())
				.build();
	}
	
	@Bean
	public Docket newUNCTApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("01. UNCT Data Api Service")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.ant("/api/**"))
				.build();
	}
	
	@Bean
	public Docket newTokenApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("09. Token Valid and validation Service")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.ant("/jwt/**"))
				.build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("표준프레임워크 DeviceAPI 연계서비스 (Hybrid App)")
                .description("표준프레임워크 하이브리드앱 실행환경  - iOS / Android 하이브리드앱 Rest 서비스")
                .termsOfServiceUrl("https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:hyb:gate_page")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.egovframe.go.kr")
                .version("3.10")
                .build();
	}
	
	
	
	
}
