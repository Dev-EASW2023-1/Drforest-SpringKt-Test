package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.dto.ListRegionResponse
import kr.easw.drforestspringkt.model.dto.UpdateRegionToUserResponse
import kr.easw.drforestspringkt.model.entity.RegionEntity
import kr.easw.drforestspringkt.model.repository.RegionRepository
import kr.easw.drforestspringkt.model.repository.UserAccountRepository
import kr.easw.drforestspringkt.model.repository.UserDataRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class RegionManagementService(
    val regionRepository: RegionRepository,
    val userAccountRepository : UserAccountRepository,
    val userDataRepository : UserDataRepository
    ) {
    fun listRegion(): List<RegionEntity> {
        return regionRepository.getAllBy()
    }

    fun listRegionResponse(): ListRegionResponse {
        return ListRegionResponse(listRegion().map { x -> x.regionName })
    }

    fun registerRegion(name: String): Boolean {
        if (getRegion(name) != null)
            return false
        regionRepository.save(RegionEntity(regionName = name))
        return true
    }

    fun deleteRegion(name: String): Boolean {
        val region = getRegion(name) ?: return false
        regionRepository.delete(region)
        return true
    }

    fun getRegion(region: String): RegionEntity? {
        return regionRepository.getByRegionName(region).orElseGet { null }
    }

    fun updateRegionToUser(region: String, userId: String): UpdateRegionToUserResponse {
        val user = userAccountRepository.findByUserId(userId).orElseThrow {
            BadCredentialsException("해당 유저를 찾을 수 없습니다.")
        }

        if (getRegion(region) == null)
            return UpdateRegionToUserResponse(false, "등록되지 않은 지역입니다.")

        userDataRepository.updateRegion(getRegion(region)!!, user)
        return UpdateRegionToUserResponse(true,"지역 이전에 성공하였습니다.")
    }
}