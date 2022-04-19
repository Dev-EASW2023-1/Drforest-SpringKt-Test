package kr.easw.drforestspringkt.model.dto

import java.util.*


data class ListRegionResponse(val regions: List<String>)


data class CheckTokenValidDto(val token: String)
data class CheckTokenValidResponse(val isValid: Boolean)

data class AnnouncementDto(val id: Long, val time: Date, val title: String, val content: String)

data class AnnouncementResponse(val announcement: Map<Long, AnnouncementDto>)


data class RefreshTokenDto(val refreshToken: String)

data class RefreshTokenResponse(val token: String)
