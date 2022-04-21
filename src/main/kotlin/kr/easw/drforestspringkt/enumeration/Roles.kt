package kr.easw.drforestspringkt.enumeration

import org.springframework.security.core.GrantedAuthority

enum class Roles(private val authorityName: String) : GrantedAuthority {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), API("ROLE_API");

    override fun getAuthority(): String {
        return authorityName
    }


}