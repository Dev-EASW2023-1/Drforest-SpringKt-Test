package kr.easw.drforestspringkt.configuration

import kr.easw.drforestspringkt.auth.JwtAuthenticateFilter
import kr.easw.drforestspringkt.enumeration.Roles
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfiguration(val filter: JwtAuthenticateFilter) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
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
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
    }

    private fun SessionManagementConfigurer<HttpSecurity>.configureSessionManagement() {
        sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    private fun ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.configureRoutePermission() {
        antMatchers("/api/public/**").permitAll()
        antMatchers("/api/auth/**").permitAll()
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