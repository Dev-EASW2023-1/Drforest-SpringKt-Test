package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.repository.UserAccountRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@Slf4j
class UserDetailService(private val userRepo: UserAccountRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepo.findByUserId(username)
            .orElseThrow { UsernameNotFoundException("") }
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