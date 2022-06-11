package kr.easw.drforestspringkt.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import kr.easw.drforestspringkt.controller.rest.AdminApiController
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Drforest Admin API",
        description = "닥터포레스트 어드민 API",
        version = "v1"
    )
)
class SpringDocConfiguration {

    // No open api for public use
//    @Bean
//    fun publicApi(): GroupedOpenApi? {
//        return GroupedOpenApi.builder()
//            .group("springshop-public")
//            .pathsToMatch("/public/**")
//            .build()
//    }

    @Bean
    fun adminApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()

            .group("drforest-admin-api")
            .pathsToMatch("/api/admin/**")
            .addOpenApiMethodFilter { method -> method.declaringClass == AdminApiController::class.java }
            .addOpenApiCustomiser {
                it.addSecurityItem(
                    SecurityRequirement()
                        .addList("JWT Token Authorization")
                ).components.addSecuritySchemes(
                    "JWT",
                    SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .`in`(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer")
                )
            }
            .build()
    }
}