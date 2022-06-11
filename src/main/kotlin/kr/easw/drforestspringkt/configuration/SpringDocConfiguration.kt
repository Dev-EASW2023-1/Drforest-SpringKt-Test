package kr.easw.drforestspringkt.configuration

import kr.easw.drforestspringkt.controller.rest.AdminApiController
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
            .build()
    }
}