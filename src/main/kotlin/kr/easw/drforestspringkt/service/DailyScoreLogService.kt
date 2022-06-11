package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.entity.DailyUserDataEntity
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.repository.DailyUserDataRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class DailyScoreLogService(val repo: UserActivityDataService, val dailyScoreRepository: DailyUserDataRepository) {

    @Scheduled(cron = "0 0 9 * * * *", zone = "KST")
    fun logScore() {
        logScoreNow()
    }

    fun fetchRecentUsers(): List<UserAccountEntity> {
        return repo.findAllRecentUser(TimeUnit.DAYS.toMillis(1))
    }

    fun logScoreNow() {
        val dataToSave = mutableListOf<DailyUserDataEntity>()
        for (x in fetchRecentUsers()) {
            repo.calculateTodayScore(x.userId, 0, 0, false).score.forEach {
                dataToSave += DailyUserDataEntity(x, it.key, it.value)
            }
        }
        dailyScoreRepository.saveAllAndFlush(dataToSave)
    }
}