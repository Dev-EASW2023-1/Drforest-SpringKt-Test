package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import org.springframework.data.jpa.repository.JpaRepository

@org.springframework.stereotype.Repository
interface AnnouncementRepository : JpaRepository<AnnouncementEntity, Long> {

//    fun findById(id: Long): Optional<AnnouncementEntity>


    fun getAllByRegionOrderByIdDesc(region: String?): List<AnnouncementEntity>



}