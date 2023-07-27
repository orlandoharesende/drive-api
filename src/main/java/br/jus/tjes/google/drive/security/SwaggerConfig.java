package br.jus.tjes.google.drive.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
    			.securitySchemes(Arrays.asList(new ApiKey("JWT", "Authorization", "header")))
    			.securityContexts(Arrays.asList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.jus.tjes.google.drive.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.getApiInfo());
    }

    private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(Arrays.asList(
				new SecurityReference("JWT",new AuthorizationScope[] { new AuthorizationScope("global", "accessEverything") })))
			.forPaths(PathSelectors.any()).build();
    }

    private ApiInfo getApiInfo(){
		return new ApiInfo("ecarta-service", "Serviço responsável pela integração entre os sistemas PJe e o serviço eCarta", "1.0.0", 
				"", new Contact(null, null, null), "", "", Collections.emptyList());

    }

}
