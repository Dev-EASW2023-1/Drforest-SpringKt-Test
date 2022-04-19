package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    fun findByUserId(userId: String): Optional<UserAccountEntity>

    fun findByUserIdAndPassword(userId: String, password: String): Optional<UserAccountEntity>

    fun existsByUserId(userId: String): Boolean

}