package kr.easw.drforestspringkt.configuration

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import kr.easw.drforestspringkt.controller.rest.AdminApiController
import kr.easw.drforestspringkt.controller.rest.AuthApiController
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration

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
                it.info(
                    Info()
                        .title("Drforest Admin API")
                        .description("닥터포레스트 어드민 API")
                        .version("v1")
                ).addSecurityItem(
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

    @Bean
    fun authApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("drforest-auth-api")
            .pathsToMatch("/api/auth/**", "/api/public/**")
            .addOpenApiMethodFilter { method -> method.declaringClass == AuthApiController::class.java }
            .addOpenApiCustomiser {
                it.info(
                    Info()
                        .title("Drforest Authorization API")
                        .description("닥터포레스트 인증 API")
                        .version("v1")
                )
            }
            .build()
    }
}