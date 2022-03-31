package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.QnAEntity
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QnARepository : JpaRepository<QnAEntity, Int> {
    fun getByUser(user: UserAccountEntity): Optional<QnAEntity>

    fun getAllByUser(user: UserAccountEntity): List<QnAEntity>
}