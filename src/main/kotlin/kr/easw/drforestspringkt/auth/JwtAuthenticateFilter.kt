package kr.easw.drforestspringkt.auth

import kr.easw.drforestspringkt.enumeration.Roles
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.service.AuthenticateService
import kr.easw.drforestspringkt.service.UserDataService
import kr.easw.drforestspringkt.service.UserDetailService
import kr.easw.drforestspringkt.util.JwtUtil
import kr.easw.drforestspringkt.util.JwtUtil.ValidateStatus
import kr.easw.drforestspringkt.util.JwtUtil.ValidateStatus.*
import kr.easw.drforestspringkt.util.logInfo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.List
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthenticateFilter(val userDetailService: UserDetailService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        println(request.headerNames.toList())
        if (request.getHeader("Authorization") != null) {
            // JWT Authorization request..
            val token = request.getHeader("Authorization")
            when (JwtUtil.validateToken(token)) {
                VALID -> {
                    val userName: String = JwtUtil.getUserFromToken(token)
                    val details = userDetailService.loadUserByUsername(userName)
                    logInfo("Processing login with JWT token - Detail $details")
                    val passToken =
                        UserAccountToken(
                            details, details.password, Roles.API
                        )
                    passToken.details = details
                    SecurityContextHolder.getContext().authentication = passToken
                    filterChain.doFilter(request, response)
                }
                INVALID -> request.setAttribute("Unauthorized", "401 Token invalid")
                EXPIRED -> request.setAttribute("Unauthorized", "401 JWT token expired")
                UNSUPPORTED -> request.setAttribute(
                    "Unauthorized",
                    "401 Current JWT Token format not supported"
                )
            }
            return
        }
        filterChain.doFilter(request, response)
    }


    private fun getJwtFromRequest(token: String?): String? {
        if (token == null) return null
        return if (token.startsWith("Bearer ")) {
            token.substring("Bearer ".length)
        } else null
    }
}