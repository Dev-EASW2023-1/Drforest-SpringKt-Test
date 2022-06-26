package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.entity.DailyUserDataEntity
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.repository.DailyUserDataRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class DailyScoreLogService(val repo: UserActivityDataService, val dailyScoreRepository: DailyUserDataRepository) {

    @Scheduled(cron = "0 0 9 * * *", zone = "GMT+9")
    fun logScore() {
        logScoreNow()
    }

    fun fetchRecentUsers(): List<UserAccountEntity> {
        return repo.findAllRecentUser(TimeUnit.DAYS.toMillis(1))
    }

    @Transactional
    fun logScoreNow() {
        // 1 day
        val timeDay = TimeUnit.DAYS.toMillis(1)
        // UTC+0 Based time
        var time = System.currentTimeMillis()
        // If time is AM, truncate to 00:00
        if (time % timeDay < timeDay / 2) {
            time -= time % timeDay
        }
        // If time is PM, truncate to 24:00
        else {
            time += (timeDay - time % timeDay)
        }
        val dataToSave = mutableListOf<DailyUserDataEntity>()
        for (x in fetchRecentUsers()) {
            repo.calculateTodayScore(x.userId, 0, TimeUnit.DAYS.toMillis(1), true).score.forEach {
                dataToSave += DailyUserDataEntity(x, it.key, it.value, time)
            }
        }
        dailyScoreRepository.saveAllAndFlush(dataToSave)
    }
}