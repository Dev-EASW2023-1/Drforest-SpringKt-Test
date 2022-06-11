package kr.easw.drforestspringkt.model.dto

import kr.easw.drforestspringkt.annotations.Beta
import java.util.*


data class ListRegionResponse(val regions: List<String>)


data class CheckTokenValidRequest(val token: String)
data class CheckTokenValidResponse(val isValid: Boolean)


data class AnnouncementResponse(val announcement: Map<Long, AnnouncementData>)


data class RefreshTokenRequest(val refreshToken: String)

data class RefreshTokenResponse(val token: String)

data class AnnouncementData(
    val id: Long,
    val time: Date,
    val author: String,
    val region: String?,
    val title: String,
    val content: String
)

data class ChangeUserDataUsingPhoneNumberRequest(
    val userName: String,
    val phoneNumber: String?,
    val changedPassword: String
)

@Beta
data class FindUserByPhoneNumberResponse(val isSuccess: Boolean, val userId: String?)
