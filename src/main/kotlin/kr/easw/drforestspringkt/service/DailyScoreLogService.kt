package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class DailyScoreLogService(val repo: UserActivityDataRepository) {

    @Scheduled(cron = "0 0 9 * * * *")
    fun logScore() {
        logScoreNow()
    }

    fun fetchRecentUsers(): List<String> {
        return repo.findAllRecentUser(Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))).map {
            it.userId
        }
    }

    fun logScoreNow(vararg users: String) {

    }
}