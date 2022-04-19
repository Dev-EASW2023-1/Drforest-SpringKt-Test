package kr.easw.drforestspringkt.service

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

    fun registerRegion(name: String) {
        regionRepository.save(RegionEntity(regionName = name))
    }

    fun deleteRegion(name: String) {
        regionRepository.delete(RegionEntity(regionName = name))
    }

    fun getRegion(region: String): RegionEntity? {
        return regionRepository.getByRegionName(region).orElseGet { null }
    }

}