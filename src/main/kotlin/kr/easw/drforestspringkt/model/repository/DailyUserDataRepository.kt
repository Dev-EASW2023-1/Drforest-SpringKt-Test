package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.DailyUserDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DailyUserDataRepository : JpaRepository<DailyUserDataEntity, Long> {
}