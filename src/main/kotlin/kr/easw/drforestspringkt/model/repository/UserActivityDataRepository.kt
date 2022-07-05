package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserActivityDataRepository : JpaRepository<UserActivityDataEntity, Long> {
    fun getAllByFieldNameAndTimestampBetween(
        fieldName: String,
        from: Date,
        to: Date
    ): List<UserActivityDataEntity>

    fun getAllByEntity_UserIdAndTimestampBetween(
        userName: String,
        from: Date,
        to: Date
    ): List<UserActivityDataEntity>

    fun getAllByFieldName(fieldName: String)

    @Query("select u.entity from UserActivityDataEntity u where u.timestamp >= :#{#timeAtLeast} AND u.timestamp <= :#{#timeAtEnd} group by u.entity")
    fun findAllRecentUserBetween(@Param("timeAtLeast") timeAtLeast: Date, @Param("timeAtEnd") timeAtEnd : Date) : List<UserAccountEntity>

    @Transactional
    fun deleteAllByEntity_UserId(userId: String)
}