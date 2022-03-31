package kr.easw.drforestspringkt.model.dto

data class CheckAccountDuplicateDto(val account: String)
data class CheckAccountDuplicateResponse(val duplicated: Boolean)

data class LoginDataDto(val id: String, val pw: String)
data class LoginDataResponse(val token: String, val refreshToken: String)


data class RegisterDataDto(val accountData: AccountDataDto, val userData: UserDataDto)
data class RegisterDataResponse(val sucess: Boolean, val message: String)


data class RefreshTokenDto(val refreshToken: String)
data class RefreshTokenResponse(val token: String)

data class CheckTokenValidDto(val token: String)
data class CheckTokenValidResponse(val isValid: Boolean)

data class AccountDataDto(val id: String, val pw: String)

data class UserDataDto(val region: String, val nickName: String, val age: Int)
