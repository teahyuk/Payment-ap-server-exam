package com.teahyuk.payment.ap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("local")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    private static final String API_DOC_TITLE = "Payment rest api";
    private static final String API_DOC_DESCRIPTION = "Payment 사용을 위한 rest api 문서 입니다.";
    private static final String VERSION1 = "v1";
    private static final String TEAHYUK_GITHUB_IO = "https://teahyuk.github.io";
    private static final String TEAHYUK = "teahyuk";

    @Bean
    public Docket swaggerApiV1() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false); // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title(API_DOC_TITLE)
                .description(API_DOC_DESCRIPTION)
                .license(TEAHYUK)
                .licenseUrl(TEAHYUK_GITHUB_IO)
                .version(VERSION1)
                .build();
    }
}
