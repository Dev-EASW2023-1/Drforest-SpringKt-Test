package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.SharedUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SharedUserRepository : JpaRepository<SharedUserEntity, Long> {
    fun findAllByUser_Account_UserId(userId: String): List<SharedUserEntity>
    fun findAllByUser_Account_UserIdAndTarget_Account_UserId(userId: String, targetId: String): Optional<SharedUserEntity>

}