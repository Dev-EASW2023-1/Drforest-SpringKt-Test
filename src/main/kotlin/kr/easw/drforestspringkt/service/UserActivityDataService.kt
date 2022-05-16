package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.math.abs

@Service
class UserActivityDataService(
    val authService: AuthenticateService,
    val repo: UserActivityDataRepository,
    val sharedUserService: SharedUserService
) {
    @Transactional
    fun get(entity: UserAccountData) {

    }

    @Transactional
    fun upload(@AuthenticationPrincipal entity: UserAccountData, data: UserDataUploadRequest) {
        println("User ${entity.username} uploaded / ${data.data}")
        val user = authService.toAccount(entity)
        data.data.forEach { (name, value) ->
            repo.save(
                UserActivityDataEntity(user, name, value)
            )
        }
    }

    fun fetchResult(user: String, time: Long): UserActivityContainerData {
        val end = System.currentTimeMillis()
        val data = mutableMapOf<Long, MutableMap<String, Float>>()
        repo.getAllByEntity_UserIdAndTimestampBetween(user, Date(end - time), Date(end)).forEach {
            val map = data.getOrPut(it.timestamp!!.time - it.timestamp!!.time % (1000 * 60 * 15)) {
                mutableMapOf()
            }
            map[it.fieldName] = map.getOrDefault(it.fieldName, 0f) + it.fieldValue
        }

        // @TODO Too complex lambda; Separate to extra method later
        data.values.forEach {
            it.putIfAbsent("Traffic", 0f)
            it.putIfAbsent("Step", 0f)
            it.putIfAbsent("OnOff", 0f)
            it.putIfAbsent("Idle", 0f)
            it.putIfAbsent("GPS", 0f)
            // ..Convert as temporary score map..
            val scoreMap = mutableMapOf<String, Int>()

            // Multiply 96 (Time unit is 15 minute, 96 * 15 = 1440 (1 day))
            scoreMap["GPS"] = when {
                it["GPS"]!! * 96 <= 3 * 1000 -> 1
                it["GPS"]!! * 96 <= 10 * 1000 -> 2
                it["GPS"]!! * 96 <= 60 * 1000 -> 3
                it["GPS"]!! * 96 <= 100 * 1000 -> 4
                else -> 5
            }
            scoreMap["OnOff"] = when {
                it["OnOff"]!! * 96 <= 20 -> 1
                it["OnOff"]!! * 96 <= 50 -> 2
                it["OnOff"]!! * 96 <= 90 -> 3
                it["OnOff"]!! * 96 <= 120 -> 4
                else -> 5
            }

            scoreMap["Step"] = when {
                it["Step"]!! * 96 >= 4710 -> 1
                it["Step"]!! * 96 >= 2355 -> 2
                it["Step"]!! * 96 >= 942 -> 3
                it["Step"]!! * 96 >= 471 -> 4
                else -> 5
            }

            scoreMap["Idle"] = when {
                it["Idle"]!! * 96 < 6 * 60 * 60 -> 1
                it["Idle"]!! * 96 < 7 * 60 * 60 -> 2
                it["Idle"]!! * 96 < 8 * 60 * 60 -> 3
                it["Idle"]!! * 96 < 9 * 60 * 60 -> 4
                else -> 5
            }

            scoreMap["Traffic"] = when {
                it["Traffic"]!! * 96 <= 34 * 1024 * 1024 -> 1
                it["Traffic"]!! * 96 <= 307 * 1024 * 1024 -> 2
                it["Traffic"]!! * 96 <= 887 * 1024 * 1024 -> 3
                it["Traffic"]!! * 96 <= 1024 * 1024 * 1024 -> 4
                else -> 5
            }


            scoreMap["Social"] = foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Traffic"]!!)

            scoreMap["Health"] = foldScore(scoreMap["GPS"]!!) + foldScore(scoreMap["Step"]!!, true)

            scoreMap["Mental"] = foldScore(scoreMap["OnOff"]!!) + foldScore(scoreMap["Idle"]!!)
        }

        return UserActivityContainerData(data.map { x -> UserActivityDataData(Date(x.key), x.value) })
    }

    fun calculateTodayScore(user: String): UserScoreData {
        val fetch = fetchResult(user, 1000 * 60 * 60 * 24)
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
            step >= 4710 -> 1
            step >= 2355 -> 2
            step >= 942 -> 3
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
        if (isStep && score == 2)
            return 1;
        return abs(score - 3)
    }

    fun convertToNewScore(resp: SharedUserScoreResponse) {
        val originData = resp.data
        resp.data.forEach {
            it.score
        }
    }
}