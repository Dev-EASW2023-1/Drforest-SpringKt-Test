
package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserDataEntity
import org.springframework.data.jpa.repository.JpaRepository
@Suppress("FunctionName")

interface UserDataRepository : JpaRepository<UserDataEntity, Long> {
    fun findByAccount_UserId(userId: String)
}