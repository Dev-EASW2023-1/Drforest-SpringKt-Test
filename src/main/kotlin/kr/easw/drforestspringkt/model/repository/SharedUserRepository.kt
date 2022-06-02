package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.SharedUserEntity
import kr.easw.drforestspringkt.model.entity.UserDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SharedUserRepository : JpaRepository<SharedUserEntity, Long> {
    fun findAllByUser_Account_UserId(userId: String): List<SharedUserEntity>
    fun findAllByUser_Account_UserIdAndTarget_Account_UserId(userId: String, targetId: String): List<SharedUserEntity>
    fun findAllByTarget_Account_UserId(targetId: String) : List<SharedUserEntity>

    fun findAllByUserAndTarget(user: UserDataEntity, target: UserDataEntity) : List<SharedUserEntity>

}