package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.enumeration.Roles
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
    private val encoder: BCryptPasswordEncoder,
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

    fun toAccountWithoutLogin(userName: String): UserAccountEntity {
        return userAccountRepository.findByUserId(userName).orElseThrow {
            BadCredentialsException("Bad credential")
        }
    }

    fun changePassword(
        user: UserAccountData,
        userName: String,
        beforePassword: String,
        changedPassword: String
    ): ChangeUserDataResponse {
        if (toAccount(user).userId != userName) {
            return ChangeUserDataResponse(toAccount(user).userId, "???????????? ???????????? ????????????.")
        }
        if (!encoder.matches(beforePassword, toAccount(user).password)) {
            return ChangeUserDataResponse(toAccount(user).userId, "?????? ??????????????? ???????????? ????????????.")
        }
        userAccountRepository.save(
            toAccount(user).apply {
                this.password = encoder.encode(changedPassword)
            }
        )
        return ChangeUserDataResponse(toAccount(user).userId, "${toAccount(user).userId}?????? ??????????????? ?????????????????????.")
    }

    fun changePasswordUsingPhoneNumber(
        userName: String,
        phoneNumber: String?,
        changedPassword: String
    ): ChangeUserDataResponse {
        val userData =
            userDataRepository.findByAccount_UserId(userName).orElseThrow { BadCredentialsException("Not found") }
        if (userData.phone != phoneNumber) {
            ChangeUserDataResponse(userName, "???????????? ??????????????? ???????????? ????????????.")
        }
        userAccountRepository.save(
            toAccountWithoutLogin(userName).apply {
                this.password = encoder.encode(changedPassword)
            }
        )
        return ChangeUserDataResponse(userName, "${userName}?????? ??????????????? ?????????????????????.")
    }

    fun refreshToken(dto: RefreshTokenRequest): RefreshTokenResponse {
        if (!JwtUtil.validateRefreshToken(dto.refreshToken).valid) {
            throw BadCredentialsException("?????????????????? ????????? JWT ??????")
        }
        val user = JwtUtil.getUserFormRefreshToken(dto.refreshToken)
        logInfo("?????? ${user}??? ?????? ????????? ?????? ?????????..")
        userAccountRepository.findByUserId(user).orElseThrow {
            logInfo("????????? ${user}??? ?????? ????????? ????????? ????????????????????? : ???????????? ?????? ??????????????????.")
            BadCredentialsException("????????? ???????????? ??????")
        }.apply {
            return RefreshTokenResponse(JwtUtil.generateToken(user))
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
            return RegisterDataResponse(false, "ID??? ???????????????.")
        }
        // TODO ?????? ?????? ?????? ??????
        val region = regionManagementService.getRegion(data.userData.region)
            ?: return RegisterDataResponse(false, "????????? ???????????????.")
        val user = userAccountRepository.save(
            UserAccountEntity(
                data.accountData.id,
                encoder.encode(data.accountData.pw),
                Roles.allow(Roles.USER)
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
        return RegisterDataResponse(true, "??????????????? ?????????????????????.")
    }

    fun findUserByName(userName: String): UserAccountEntity? {
        return userAccountRepository.findByUserId(userName).orElseGet { null }
    }


    fun findUserDataByName(userName: String): UserDataEntity? {
        return userDataRepository.findByAccount_UserId(userName).orElseGet { null }
    }

    fun getUserData(user: UserAccountData): UserDataDto {
        val userData =
            userDataRepository.findByAccount_UserId(user.username).orElseThrow { BadCredentialsException("Not found") }
        return UserDataDto(userData.region.regionName, userData.name, userData.phone)
    }

    fun getUserCreatedTimeData(user: UserAccountData): UserCreatedTimeDataDto {
        val userData =
            userDataRepository.findByAccount_UserId(user.username).orElseThrow { BadCredentialsException("Not found") }
        return UserCreatedTimeDataDto(userData.createdTimestamp!!)
    }

    fun findAccountByUserId(userId: String): UserDataEntity? {
        return userDataRepository.findByAccount_UserId(userId).orElseGet { null }
    }

    fun findAllUser(): List<UserDataEntity> {
        return userDataRepository.findAll()
    }

    fun fromEntity(entity: UserDataEntity): UserAccountData {
        return UserAccountData(
            entity.account.userId,
            entity.account.password,
            true,
            accountNonExpired = true,
            credentialsNonExpired = true,
            accountNonLocked = true
        )
    }

    fun findUserByPhone(phoneNumber: String): UserDataEntity? {
        return userDataRepository.findByPhone(phoneNumber).orElseGet { null }
    }

    fun addPermission(userName: String, role: Roles) {
        userAccountRepository.findByUserId(userName).ifPresent {
            it.permission = role.allow(it.permission)
            userAccountRepository.save(it)
        }
    }

    fun removePermission(userName: String, role: Roles) {
        userAccountRepository.findByUserId(userName).ifPresent {
            it.permission = role.disallow(it.permission)
            userAccountRepository.save(it)
        }
    }

}