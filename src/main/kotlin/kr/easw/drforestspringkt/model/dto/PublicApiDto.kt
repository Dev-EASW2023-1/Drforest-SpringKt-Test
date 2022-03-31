package kr.easw.drforestspringkt.model.dto


data class ListRegionResponse(val regions: List<String>)


data class CheckTokenValidDto(val token: String)
data class CheckTokenValidResponse(val isValid: Boolean)