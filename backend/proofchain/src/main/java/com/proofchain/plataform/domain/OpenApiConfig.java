package com.proofchain.plataform.domain;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração global da documentação OpenAPI.
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "ProofChain API",
                description = "API responsável pelo gerenciamento de usuários e instituições",
                version = "1.0.0",
                contact = @Contact(
                        name = "Equipe ProofChain",
                        email = "contato@proofchain.com"
                )
        )
)

public class OpenApiConfig {
}
