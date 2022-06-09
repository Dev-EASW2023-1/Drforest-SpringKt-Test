package kr.easw.drforestspringkt.model.dto

import java.util.*

data class CheckAccountDuplicateResponse(val duplicated: Boolean)

data class LoginDataRequest(val id: String, val pw: String)
data class LoginDataResponse(val token: String, val refreshToken: String)


data class RegisterDataRequest(val accountData: AccountDataDto, val userData: UserDataDto)

data class RegisterDataResponse(val sucess: Boolean, val message: String)


data class AccountDataDto(val id: String, val pw: String)

data class UserDataDto(val region: String, val nickName: String, val phone: String)

data class UserCreatedTimeDataDto(val timestamp: Date)