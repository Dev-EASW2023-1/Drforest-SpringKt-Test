package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.DailyUserDataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DailyUserDataRepository : JpaRepository<DailyUserDataEntity, Long> {
}