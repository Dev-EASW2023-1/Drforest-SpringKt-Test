package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.repository.UserDataRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class UserDataService(val userDataRepository: UserDataRepository) {
    fun changeFCMToken(data: UserAccountData, token: String) {
        val user = userDataRepository.findByAccount_UserId(data.username)
            .orElseThrow { BadCredentialsException("Bad credential") }
        user.fcmToken = token
        userDataRepository.save(user)
    }

}