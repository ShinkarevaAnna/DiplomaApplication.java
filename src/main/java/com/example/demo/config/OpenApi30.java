package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @Info(title = "ITMO", version = "1.0"))
public class OpenApi30 {
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public-api")
//                .pathsToMatch("/api/**")
//                .build();
//    }

//    @Bean
//    public SwaggerUiConfigParameters swaggerUiConfigParameters() {
//        return new SwaggerUiConfigParametersBuilder()
//                .docExpansion(SwaggerUiConfigParameters.DocExpansion.LIST)
//                .defaultModelRendering(SwaggerUiConfigParameters.ModelRendering.EXAMPLE)
//                .build();
//    }

//    @Bean
//    public SwaggerUiOAuthProperties swaggerUiOAuthProperties() {
//        SwaggerUiOAuthProperties properties = new SwaggerUiOAuthProperties();
//        properties.setClientId("your-client-id");
//        properties.setClientSecret("your-client-secret");
//        properties.setAppName("Your App Name");
//        return properties;
//    }

}
