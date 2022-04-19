package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import javax.persistence.Table

@Suppress("FunctionName")
@Table(name = "UserData")
interface UserDataRepository : JpaRepository<UserDataEntity, Long> {
    fun findByAccount_UserId(id: String): Optional<UserDataEntity>


}