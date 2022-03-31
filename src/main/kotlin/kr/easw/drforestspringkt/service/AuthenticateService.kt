package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.repository.UserAccountRepository
import kr.easw.drforestspringkt.util.JwtUtil
import kr.easw.drforestspringkt.util.logInfo
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AuthenticateService(
    private val userAccountRepository: UserAccountRepository,
) {
    fun login(dto: LoginDataDto): LoginDataResponse {
        userAccountRepository.findByUserIdAndPassword(dto.id, dto.pw)
            .orElseThrow { BadCredentialsException("Bad credential") }.apply {
                return LoginDataResponse(
                    JwtUtil.generateToken(this.userId),
                    JwtUtil.generateRefreshToken(this.userId)
                )
            }
    }

    fun toAccount(user: UserAccountData) : UserAccountEntity {
        return userAccountRepository.findByUserId(user.username).orElseThrow {
            BadCredentialsException("Bad credential")
        }
    }


    fun refreshToken(dto: RefreshTokenDto): RefreshTokenResponse {
        if (!JwtUtil.validateRefreshToken(dto.refreshToken).valid) {
            throw BadCredentialsException("만료되었거나 잘못된 JWT 토큰")
        }
        val user = JwtUtil.getUserFromToken(dto.refreshToken)
        logInfo("유저 ${user}의 토큰 재발급 요청 처리중..")
        userAccountRepository.findByUserId(user).orElseThrow {
            logInfo("사용자 ${user}의 토큰 재발급 요청이 거부되었습니다 : 등록되지 않은 사용자입니다.")
            BadCredentialsException("사용자 존재하지 않음")
        }.apply {
            return RefreshTokenResponse(JwtUtil.generateRefreshToken(user))
        }
    }

    fun checkValidationResponse(dto: CheckTokenValidDto): CheckTokenValidResponse {
        return CheckTokenValidResponse(JwtUtil.validateToken(dto.token).valid)
    }

    fun isAccountExists(name: String): Boolean {
        return userAccountRepository.existsByUserId(name)
    }

    fun checkAccountDuplicatesResponse(id: String): CheckAccountDuplicateResponse {
        return CheckAccountDuplicateResponse(isAccountExists(id))
    }

}