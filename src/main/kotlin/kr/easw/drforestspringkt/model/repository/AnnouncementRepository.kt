package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AnnouncementRepository : JpaRepository<AnnouncementEntity, Long> {

    fun getById(id: Long): Optional<AnnouncementEntity>

    fun getAllByOrderByIdDesc() : List<AnnouncementEntity>
}