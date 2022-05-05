package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.QnAEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Suppress("FunctionName")
@Repository
interface QnARepository : JpaRepository<QnAEntity, Int> {
    fun getByUser_UserId(id: String): Optional<QnAEntity>

    fun getAllByUser_UserId(id: String): List<QnAEntity>
}