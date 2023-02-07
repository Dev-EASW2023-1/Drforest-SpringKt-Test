package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.entity.DailyUserDataEntity
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.entity.UserDataEntity
import kr.easw.drforestspringkt.model.entity.UserNoticeEntity
import kr.easw.drforestspringkt.model.repository.DailyUserDataRepository
import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import kr.easw.drforestspringkt.model.repository.UserNoticeRepository
import kr.easw.drforestspringkt.util.FCMUtility
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class DailyScoreLogService(
    val repo: UserActivityDataService,
    val dailyScoreRepository: DailyUserDataRepository,
    val userActivityDataRepository: UserActivityDataRepository,
    val authenticateService: AuthenticateService,
    val userNoticeRepository: UserNoticeRepository
    ) {

    @Scheduled(cron = "0 0 9 * * *", zone = "GMT+9")
    fun logScore() {
        logScoreNow()
    }

    @Scheduled(cron = "0 0 0/6 * * *")
    fun notSendDataUsers() {
        onFCMSendPushLooped()
    }

    fun fetchRecentUsers(): List<UserAccountEntity> {
        return repo.findAllRecentUser(TimeUnit.DAYS.toMillis(1))
    }

    fun fetchRecentUsersForFCM(): List<UserAccountEntity> { // From duration to the present.
        return repo.findAllRecentUserForFCM(TimeUnit.DAYS.toMillis(10))
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

    @Transactional
    fun onFCMSendPushLooped() { // It also sends FCM push notifications.
        val usersToBePushed = mutableListOf<String>()
        val usersToBeWritten = mutableListOf<UserDataEntity?>()

        for (x in fetchRecentUsersForFCM()) {
            if(!userActivityDataRepository.existsByEntity_UserIdAndTimestampBetween(
                    x.userId,
                    Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(6)),
                    Date(System.currentTimeMillis()))){
                usersToBePushed += x.userId
                usersToBeWritten += authenticateService.findAccountByUserId(x.userId)
            }
        }

        for(user in usersToBeWritten) {
            userNoticeRepository.save( // Write notifications.
                UserNoticeEntity(
                    user!!,
                    "측정된 데이터가 알 수 없는 원인으로 전송에 실패하였습니다.\n(" +
                            getFormattedTime(Date(System.currentTimeMillis()
                                    + TimeUnit.HOURS.toMillis(9) - TimeUnit.HOURS.toMillis(6))) // Measuring interval(Start)
                            + " ~ " +
                            getFormattedTime(Date(System.currentTimeMillis()
                                    + TimeUnit.HOURS.toMillis(9))) + ")", // Measuring interval(End)
                    false
                )
            )
        }

        for(user in usersToBePushed){
            FCMUtility.sendPush(user,
                "정신 측정 알림",
                "측정된 데이터가 알 수 없는 원인으로 전송에 실패하였습니다.\n(" +
                        getFormattedTime(Date(System.currentTimeMillis()
                                + TimeUnit.HOURS.toMillis(9) - TimeUnit.HOURS.toMillis(6))) // Measuring interval(Start)
                        + " ~ " +
                        getFormattedTime(Date(System.currentTimeMillis()
                                + TimeUnit.HOURS.toMillis(9))) // Measuring interval(End)
                        + ")\n눌러서 다시 로그인 해주세요.", 2
            )
        }
    }

    fun getFormattedTime(date: Date): String { // Change time to desired format with SimpleDateFormat.
        val formatter = SimpleDateFormat("MM월 dd일 HH:mm", Locale("ko", "KR"))
        return formatter.format(date)
    }
}