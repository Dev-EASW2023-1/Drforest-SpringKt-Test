package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.entity.UserDataEntity
import kr.easw.drforestspringkt.model.repository.UserAccountRepository
import kr.easw.drforestspringkt.model.repository.UserDataRepository
import kr.easw.drforestspringkt.util.JwtUtil
import kr.easw.drforestspringkt.util.logInfo
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticateService(
    private val userAccountRepository: UserAccountRepository,
    private val userDataRepository: UserDataRepository,
    private val regionManagementService: RegionManagementService,
    private val encoder: BCryptPasswordEncoder
) {
    fun login(dto: LoginDataRequest): LoginDataResponse {
        userAccountRepository.findByUserId(dto.id)
            .orElseThrow { BadCredentialsException("Bad credential") }.apply {
                if (!encoder.matches(dto.pw, password)) {
                    throw BadCredentialsException("Bad credential")
                }
                return LoginDataResponse(
                    JwtUtil.generateToken(this.userId),
                    JwtUtil.generateRefreshToken(this.userId)
                )
            }
    }

    fun toAccount(user: UserAccountData): UserAccountEntity {
        return userAccountRepository.findByUserId(user.username).orElseThrow {
            BadCredentialsException("Bad credential")
        }
    }

    fun changePassword(user: UserAccountData, password: String) {
        userAccountRepository.save(
            toAccount(user).apply {
                this.password = encoder.encode(password)
            }
        )
    }

    fun refreshToken(dto: RefreshTokenRequest): RefreshTokenResponse {
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

    fun checkValidationResponse(dto: CheckTokenValidRequest): CheckTokenValidResponse {
        return CheckTokenValidResponse(JwtUtil.validateToken(dto.token).valid)
    }

    fun isAccountExists(name: String): Boolean {
        return userAccountRepository.existsByUserId(name)
    }

    fun checkAccountDuplicatesResponse(id: String): CheckAccountDuplicateResponse {
        return CheckAccountDuplicateResponse(isAccountExists(id))
    }

    fun register(data: RegisterDataRequest): RegisterDataResponse {
        if (isAccountExists(data.accountData.id)) {
            return RegisterDataResponse(false, "ID가 중복됩니다.")
        }
        // TODO 추가 보안 절차 추가
        val region = regionManagementService.getRegion(data.userData.region)
            ?: return RegisterDataResponse(false, "잘못된 지역입니다.")
        val user = userAccountRepository.save(
            UserAccountEntity(
                data.accountData.id,
                encoder.encode(data.accountData.pw)
            )
        )
        userDataRepository.save(
            UserDataEntity(
                user,
                data.userData.nickName,
                data.userData.phone,
                region
            )
        )
        return RegisterDataResponse(true, "회원가입에 성공하였습니다.")
    }

    fun findUserByName(userName: String): UserAccountEntity? {
        return userAccountRepository.findByUserId(userName).orElseGet { null }
    }

    fun getUserData(user: UserAccountData) : UserDataDto{
        val userData = userDataRepository.findByAccount_UserId(user.username).orElseThrow { BadCredentialsException("Not found") }
        return UserDataDto(userData.region.regionName, userData.name, userData.phone)
    }

    fun findAccountByUserId(userId: String): UserDataEntity? {
        return userDataRepository.findByAccount_UserId(userId).orElseGet { null }
    }

    fun findAllUser() : List<UserDataEntity> {
        return userDataRepository.findAll()
    }

}