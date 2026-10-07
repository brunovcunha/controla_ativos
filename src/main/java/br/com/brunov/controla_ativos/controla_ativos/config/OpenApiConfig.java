package br.com.brunov.controla_ativos.controla_ativos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Controla Ativos API",
                version = "1.1",
                description = "API para gerenciamento de ativos, funcionários, empréstimos e departamentos."
        )
)
public class OpenApiConfig {
}