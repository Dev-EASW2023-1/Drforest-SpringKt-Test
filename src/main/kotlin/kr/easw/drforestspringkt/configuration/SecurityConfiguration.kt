package kr.easw.drforestspringkt.configuration

import kr.easw.drforestspringkt.auth.JwtAuthenticateFilter
import kr.easw.drforestspringkt.enumeration.Roles
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

// IJ에서 인식을 못하여 오류 발생, Suppression으로 해결
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@EnableWebSecurity
@Configuration
class SecurityConfiguration(val jwtFilter : JwtAuthenticateFilter) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        println("Filter: $jwtFilter")
        http
            .sessionManagement {
                it.configureSessionManagement()
            }
            .authorizeRequests {
                it.configureRoutePermission()
            }
            .logout {
                it.configureLogout()
            }
            .csrf {
                it.configureCsrf()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    }


    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers("/static/**")

    }

    private fun SessionManagementConfigurer<HttpSecurity>.configureSessionManagement() {
        sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.configureRoutePermission() {
        antMatchers("/api/public/**").permitAll()
        antMatchers("/api/auth/**").permitAll()
        // 임시로 허용
        antMatchers("/api/admin/**").permitAll()
        antMatchers("/api/**").hasAnyRole(Roles.API.name)
        antMatchers("/**").permitAll()
        antMatchers("/board/admin/**").hasAnyRole(Roles.ADMIN.name)
        antMatchers("/board/**").hasAnyRole(Roles.USER.name)
    }


    private fun LogoutConfigurer<HttpSecurity>.configureLogout() {

    }

    private fun CsrfConfigurer<HttpSecurity>.configureCsrf() {
        disable()
    }


}