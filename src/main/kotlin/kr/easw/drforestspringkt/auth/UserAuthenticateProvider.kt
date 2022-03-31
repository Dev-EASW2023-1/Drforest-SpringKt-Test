package kr.easw.drforestspringkt.auth

import kr.easw.drforestspringkt.service.UserDetailService
import org.springframework.stereotype.Component

@Component
class UserAuthenticateProvider(private val userDetailSerivce: UserDetailService) {
}