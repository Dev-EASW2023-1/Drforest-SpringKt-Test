package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.dto.AddRegionRequest
import kr.easw.drforestspringkt.model.dto.AddRegionResponse
import kr.easw.drforestspringkt.model.dto.ListRegionResponse
import kr.easw.drforestspringkt.model.entity.RegionEntity
import kr.easw.drforestspringkt.model.repository.RegionRepository
import org.springframework.stereotype.Service

@Service
class RegionManagementService(val regionRepository: RegionRepository) {
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

    fun registerRegion(request: AddRegionRequest): AddRegionResponse {
        if (!registerRegion(request.regionName))
            return AddRegionResponse(false, "이미 등록된 지역입니다.")
        return AddRegionResponse(true, "지역이 등록되었습니다.")
    }

    fun deleteRegion(name: String) {
        regionRepository.delete(RegionEntity(regionName = name))
    }

    fun getRegion(region: String): RegionEntity? {
        return regionRepository.getByRegionName(region).orElseGet { null }
    }

}