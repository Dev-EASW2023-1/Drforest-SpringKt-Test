package kr.easw.drforestspringkt.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserAccountData(
    username: String,
    password: String,
    enabled: Boolean,
    accountNonExpired: Boolean,
    credentialsNonExpired: Boolean,
    accountNonLocked: Boolean,
    vararg authorities: GrantedAuthority,
) : User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities.toList())