package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Suppress("FunctionName")
@Repository
interface UserDataRepository : JpaRepository<UserDataEntity, Long> {
    fun findByAccount_UserId(id: String): Optional<UserDataEntity>


    fun findByPhone(phone: String) : Optional<UserDataEntity>
}