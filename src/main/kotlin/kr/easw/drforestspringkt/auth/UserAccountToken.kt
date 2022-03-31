package kr.easw.drforestspringkt.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAccountToken(principal: UserDetails, credentials: Any, vararg authorities: GrantedAuthority) :
    UsernamePasswordAuthenticationToken(principal, credentials, authorities.toList())