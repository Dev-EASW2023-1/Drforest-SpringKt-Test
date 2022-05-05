package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
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
}