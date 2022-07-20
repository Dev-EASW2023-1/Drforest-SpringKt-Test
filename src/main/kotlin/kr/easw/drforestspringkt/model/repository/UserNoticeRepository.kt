@file:Suppress("FunctionName")

package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserNoticeEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserNoticeRepository : JpaRepository<UserNoticeEntity, Long> {

//    fun findById(id: Long): Optional<AnnouncementEntity>


    fun getAllByUser_Account_UserIdOrderByIdDesc(userId: String, pageable: Pageable) : List<UserNoticeEntity>

    @Transactional
    fun deleteAllByUser_Account_UserId(userId: String)

}