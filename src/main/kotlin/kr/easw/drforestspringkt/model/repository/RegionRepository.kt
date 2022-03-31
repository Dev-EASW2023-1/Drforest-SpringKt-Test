package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.RegionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RegionRepository : JpaRepository<RegionEntity, Long>{
    fun getByRegionName(regionName: String) : Optional<RegionEntity>

    fun getAll() : List<RegionEntity>
}