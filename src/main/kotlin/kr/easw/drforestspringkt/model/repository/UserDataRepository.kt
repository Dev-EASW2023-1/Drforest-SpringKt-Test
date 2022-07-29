package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.RegionEntity
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.entity.UserDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Suppress("FunctionName")
@Repository
interface UserDataRepository : JpaRepository<UserDataEntity, Long> {
    fun findByAccount_UserId(id: String): Optional<UserDataEntity>

    fun findByPhone(phone: String) : Optional<UserDataEntity>

    @Transactional
    fun deleteAllByAccount_UserId(userId: String)

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserDataEntity d SET d.region = :region WHERE d.account = :account")
    @Transactional
    fun updateRegion(region: RegionEntity, account: UserAccountEntity) : Int
}