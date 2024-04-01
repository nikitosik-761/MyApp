package com.fullApp.myApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "My Api",
                description = "My App", version = "1.0.0",
                contact = @Contact(
                        name = "nikitosik-761",
                        email = "email@email.dev",
                        url = "https://exc.exc.com"
                )
        )
)
public class OpenApiConfig {
}
