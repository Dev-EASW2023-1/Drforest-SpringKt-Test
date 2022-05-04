@file:Suppress("FunctionName")

package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import kr.easw.drforestspringkt.model.entity.UserNoticeEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@org.springframework.stereotype.Repository
interface UserNoticeRepository : JpaRepository<UserNoticeEntity, Long> {

//    fun findById(id: Long): Optional<AnnouncementEntity>


    fun getAllByUser_Account_UserId(userId: String, pageable: Pageable) : List<UserNoticeEntity>


}