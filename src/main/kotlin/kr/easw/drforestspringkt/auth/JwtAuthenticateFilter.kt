package kr.easw.drforestspringkt.auth

import kr.easw.drforestspringkt.util.JwtUtil
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticateFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.getHeader("Authorization") != null) {
            return
        }
        filterChain.doFilter(request, response)
    }

}