package com.example.resonance

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Resonance API",
        version = "1.0",
        description = "Resonance Platform API\n\n" +
                "REST API для платформы взаимодействия студентов и компаний.\n\n" +
                "## Аутентификация\n\n" +
                "API использует JWT (JSON Web Token) для аутентификации.\n\n" +
                "### Как получить токен:\n" +
                "1. Зарегистрируйтесь через /auth/register/student или /auth/register/company\n" +
                "2. Войдите через /auth/login - получите accessToken и refreshToken\n" +
                "3. Используйте accessToken в заголовке: Authorization: Bearer {token}\n" +
                "4. При истечении срока действия обновите токен через /auth/refresh\n\n" +
                "### Роли:\n" +
                "- **STUDENT** - доступ к студенческим endpoints\n" +
                "- **COMPANY** - доступ к корпоративным endpoints\n" +
                "- **ADMIN** - полный доступ (в разработке)"
    ),
    servers = [
        Server(
            url = "http://localhost:8088",
            description = "Local Development"
        ),
        Server(
            url = "https://api.resonance.com",
            description = "Production"
        )
    ]
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "Введите JWT токен (только токен, без 'Bearer')"
)
class OpenApiConfig