package kr.easw.drforestspringkt.auth

import kr.easw.drforestspringkt.util.logInfo
import lombok.extern.slf4j.Slf4j
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
@Slf4j
class JWTAuthenticateProvider(
    private val service: UserDetailsService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        logInfo("Try to authenticate userName ${authentication.name}..")
        return createAuthentication(
            authentication
        )
    }

    fun createAuthentication(authentication: Authentication): Authentication {
        return UserAccountToken(service.loadUserByUsername(authentication.name),
            authentication.credentials,
            *authentication.authorities.toTypedArray())
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.isAssignableFrom(UserAccountToken::class.java)
    }
}