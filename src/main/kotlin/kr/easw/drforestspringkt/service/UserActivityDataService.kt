package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Service
class UserActivityDataService(
    val authService: AuthenticateService,
    val repo: UserActivityDataRepository,
    val sharedUserService: SharedUserService
) {
    @Transactional
    fun upload(@AuthenticationPrincipal entity: UserAccountData, data: UserDataUploadRequest) {
        val user = authService.toAccount(entity)
        data.data.forEach { (name, value) ->
            repo.save(
                UserActivityDataEntity(user, name, value)
            )
        }
    }

    @Transactional
    fun upload(@AuthenticationPrincipal entity: UserAccountData, data: BulkUserDataUploadRequest) {
        val user = authService.toAccount(entity)
        data.requests.forEach {
            it.data.forEach { (name, value) ->
                repo.save(
                    UserActivityDataEntity(user, name, value)
                )
            }
        }
    }

    fun findAllRecentUser(duration: Long): List<UserAccountEntity> {
        return repo.findAllRecentUserBetween(
            Date(System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000)) - duration),
            Date(System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000)) - duration + (24 * 60 * 60 * 1000))
        )
    }

    fun fetchResult(
        user: String,
        amount: Long,
        tick: Long,
        adjustHour: Int = 0 /* Default value = UTC + 0 */,
        endMargin: Long = 0,
        truncateToDay: Boolean = true
    ): UserActivityContainerData {
        val adjustTime = adjustHour * 1000 * 60 * 60
        var end = System.currentTimeMillis()
        var start = end - amount
        val data = mutableMapOf<Long, MutableMap<String, Float>>()
        val morningData = LinkedHashMap<Long, MutableMap<String, Float>>()
        val afternoonData = LinkedHashMap<Long, MutableMap<String, Float>>()
        // Truncation.
        // If true, end time / start time will force truncated.
        if (truncateToDay) {
            val dateThreshold = (1000 * 60 * 60 * 24)
            // Starting time will be subtracted to start time of date on UTC
            start -= start % dateThreshold
            // Ending time will be expanded to end time of date on UTC
            end += dateThreshold - (end % dateThreshold)
        }
        start -= endMargin
        end -= endMargin
        // Truncate complete. Adjusting value..
        start += adjustTime
        end += adjustTime
        repo.getAllByEntity_UserIdAndTimestampBetween(user, Date(start), Date(end)).forEach {
            val spareTime = (it.timestamp!!.time) % tick
            val halfDay = TimeUnit.DAYS.toMillis(1) / 2

            val totalMap = data.getOrPut(
                when (tick) {
                    // In the daily average graph, the day graph represents data from the previous day.
                    1000 * 60 * 60 * 24L ->
                        (it.timestamp!!.time + TimeUnit.DAYS.toMillis(1)) - spareTime
                    // In the n-minute graph, the x-axis is the time as it is.
                    else ->
                        it.timestamp!!.time - spareTime
                }
            ) {
                mutableMapOf()
            }
            totalMap[it.fieldName] = totalMap.getOrDefault(it.fieldName, 0f) + it.fieldValue

            if(tick == 1000 * 60 * 60 * 24L && it.fieldName == "Idle") { // Apply calculated Idle data only to the day graph.
                if(spareTime <= halfDay){ // AM Idle data.
                    val morningMap = morningData.getOrPut(
                        (it.timestamp!!.time + TimeUnit.DAYS.toMillis(1)) - spareTime
                    ) {
                        mutableMapOf()
                    }
                    morningMap[it.fieldName] = morningMap.getOrDefault(it.fieldName, 0f) + it.fieldValue

                } else { // PM Idle data.
                    val afternoonMap = afternoonData.getOrPut(
                        (it.timestamp!!.time + TimeUnit.DAYS.toMillis(1)) - spareTime
                    ) {
                        mutableMapOf()
                    }
                    afternoonMap[it.fieldName] = afternoonMap.getOrDefault(it.fieldName, 0f) + it.fieldValue

                }
            }
        }

        if(tick == 1000 * 60 * 60 * 24L) { // Apply calculated Idle data only to the day graph.
            data.keys.forEach {// Idle data is the largest value among AM and PM.
                val morningIdleData = morningData[it]?.getValue("Idle") ?: 0f
                val afternoonIdleData = afternoonData[it]?.getValue("Idle") ?: 0f

                if (morningIdleData > afternoonIdleData) {
                    data[it] = (data[it]!! + mapOf("Idle" to morningIdleData)) as MutableMap<String, Float>
                } else if (morningIdleData < afternoonIdleData) {
                    data[it] = (data[it]!! + mapOf("Idle" to afternoonIdleData)) as MutableMap<String, Float>
                }
            }
        }

        data.values.forEach {
            it.putIfAbsent("Traffic", 0f)
            it.putIfAbsent("Step", 0f)
            it.putIfAbsent("OnOff", 0f)
            it.putIfAbsent("Idle", 0f)
            it.putIfAbsent("GPS", 0f)
            // ..Convert as temporary score map..
            val scoreMap = mutableMapOf<String, Int>()
            val multiplier = (1000 * 60 * 60 * 24) / tick
            // Multiply 96 (Time unit is 15 minute, 96 * 15 = 1440 (1 day))
            scoreMap["GPS"] = when {
                it["GPS"]!! * multiplier <= 3 * 1000 -> 1
                it["GPS"]!! * multiplier <= 10 * 1000 -> 2
                it["GPS"]!! * multiplier <= 60 * 1000 -> 3
                it["GPS"]!! * multiplier <= 100 * 1000 -> 4
                else -> 5
            }
            scoreMap["OnOff"] = when {
                it["OnOff"]!! * multiplier <= 20 -> 1
                it["OnOff"]!! * multiplier <= 50 -> 2
                it["OnOff"]!! * multiplier <= 90 -> 3
                it["OnOff"]!! * multiplier <= 120 -> 4
                else -> 5
            }

            scoreMap["Step"] = when {
                it["Step"]!! * multiplier >= 70001 -> 1
                it["Step"]!! * multiplier >= 2356 -> 3
                it["Step"]!! * multiplier >= 943 -> 3
                it["Step"]!! * multiplier >= 471 -> 4
                else -> 5
            }

            scoreMap["Idle"] = when {
                it["Idle"]!! * multiplier < 6 * 60 * 60 -> 1
                it["Idle"]!! * multiplier < 7 * 60 * 60 -> 2
                it["Idle"]!! * multiplier < 8 * 60 * 60 -> 3
                it["Idle"]!! * multiplier < 9 * 60 * 60 -> 4
                else -> 5
            }

            scoreMap["Traffic"] = when {
                it["Traffic"]!! * multiplier <= 34 * 1024 * 1024 -> 1
                it["Traffic"]!! * multiplier <= 307 * 1024 * 1024 -> 2
                it["Traffic"]!! * multiplier <= 887 * 1024 * 1024 -> 3
                it["Traffic"]!! * multiplier <= 1024 * 1024 * 1024 -> 4
                else -> 5
            }
            it["Social"] = foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Traffic"]!!).toFloat()

            it["Health"] = foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Step"]!!, true).toFloat()

            it["Mental"] = foldScore(scoreMap["OnOff"]!!) + foldScore(scoreMap["Idle"]!!).toFloat()

            it["Total"] =
                (foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Traffic"]!!) + foldScore(
                    scoreMap["Step"]!!,
                    true
                ) + foldScore(
                    scoreMap["OnOff"]!!
                ) + foldScore(scoreMap["Idle"]!!)).toFloat()
        }

        return UserActivityContainerData(data.map { x -> UserActivityDataData(Date(x.key), x.value) })
    }


    fun calculateTodayScore(
        user: String,
        dateHourAdjust: Int = 0 /* Default value = UTC + 18 (KST + 9) */,
        endMargin: Long = 1000 * 60 * 60 * 24,
        truncateToDay: Boolean = true
    ): UserScoreData {
        val fetch = fetchResult(
            user,
            (System.currentTimeMillis() % (1000 * 60 * 60 * 24)),
            1000 * 60 * 60 * 24,
            adjustHour = dateHourAdjust,
            endMargin = endMargin,
            truncateToDay = truncateToDay
        )
        val scoreMap = mutableMapOf<String, Int>()
        val totalMap = mutableMapOf<String, Float>()
        fetch.list.forEach { data ->
            data.value.forEach {
                totalMap[it.key] = totalMap.getOrElse(it.key) { 0f } + it.value
            }
        }
        val traffic = totalMap.getOrElse("Traffic") { 0f }
        val step = totalMap.getOrElse("Step") { 0f }
        val onOff = totalMap.getOrElse("OnOff") { 0f }
        val idle = totalMap.getOrElse("Idle") { 0f }
        val gps = totalMap.getOrElse("GPS") { 0f }
        scoreMap["GPS"] = when {
            gps <= 3 * 1000 -> 1
            gps <= 10 * 1000 -> 2
            gps <= 60 * 1000 -> 3
            gps <= 100 * 1000 -> 4
            else -> 5
        }
        scoreMap["OnOff"] = when {
            onOff <= 20 -> 1
            onOff <= 50 -> 2
            onOff <= 90 -> 3
            onOff <= 120 -> 4
            else -> 5
        }

        scoreMap["Step"] = when {
            step >= 70001 -> 1
            step >= 2356 -> 3
            step >= 943 -> 3
            step >= 471 -> 4
            else -> 5
        }

        scoreMap["Idle"] = when {
            idle < 6 * 60 * 60 -> 1
            idle < 7 * 60 * 60 -> 2
            idle < 8 * 60 * 60 -> 3
            idle < 9 * 60 * 60 -> 4
            else -> 5
        }

        scoreMap["Traffic"] = when {
            traffic <= 34 * 1024 * 1024 -> 1
            traffic <= 307 * 1024 * 1024 -> 2
            traffic <= 887 * 1024 * 1024 -> 3
            traffic <= 1024 * 1024 * 1024 -> 4
            else -> 5
        }

        scoreMap["Social"] = foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Traffic"]!!)

        scoreMap["Health"] = foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Step"]!!, true)

        scoreMap["Mental"] = foldScore(scoreMap["OnOff"]!!) + foldScore(scoreMap["Idle"]!!)

        scoreMap["Total"] =
            (foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Traffic"]!!) + foldScore(
                scoreMap["Step"]!!,
                true
            ) + foldScore(
                scoreMap["OnOff"]!!
            ) + foldScore(scoreMap["Idle"]!!))
        return UserScoreData(user, scoreMap)
    }

    fun fetchSharedScore(user: UserAccountData): SharedUserScoreResponse {
        val map = mutableListOf<UserScoreData>()
        sharedUserService.findAllSharedUser(user).users.forEach {
            if (!it.isShared)
                return@forEach
            map += calculateTodayScore(it.userId).apply {
                this.name = it.userName
            }
        }
        return SharedUserScoreResponse(map)
    }


    /**
     * Fold 1-5 score to 0-2.
     * If it is step score, folding score will bi in next array: [2, 0, 0, 1, 2].
     */
    fun foldScore(score: Int, isStep: Boolean = false): Int {
//        if (isStep && score == 2)
//            return 0
        return abs(score - 3)
    }
}