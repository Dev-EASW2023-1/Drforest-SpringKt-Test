package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.repository.UserAccountRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Slf4j
class UserDetailService() : UserDetailsService {
    @Autowired
    @Lazy
    private lateinit var userRepo: UserAccountRepository

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepo.findByUserId(username)
            .orElseThrow { UsernameNotFoundException("401 Authenticate failed") }
            .run {
                UserAccountData(
                    username = username,
                    password = this.password,
                    accountNonExpired = true,
                    credentialsNonExpired = true,
                    accountNonLocked = true,
                    enabled = true
                )
            }
    }

}