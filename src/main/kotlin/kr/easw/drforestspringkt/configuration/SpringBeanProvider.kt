package kr.easw.drforestspringkt.configuration

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class SpringBeanProvider {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}