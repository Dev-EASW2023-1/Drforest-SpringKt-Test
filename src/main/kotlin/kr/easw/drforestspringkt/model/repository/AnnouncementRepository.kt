package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@org.springframework.stereotype.Repository
interface AnnouncementRepository : JpaRepository<AnnouncementEntity, Long> {

//    fun findById(id: Long): Optional<AnnouncementEntity>


    fun getAllByOrderByIdDesc() : List<AnnouncementEntity>


}