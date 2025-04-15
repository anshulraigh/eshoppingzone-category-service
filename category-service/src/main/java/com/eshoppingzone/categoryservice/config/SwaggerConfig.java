package com.eshoppingzone.categoryservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Category Service API",
                version = "1.0",
                description = "API for managing product categories in eShoppingZone"
        )
)
public class SwaggerConfig {
}
