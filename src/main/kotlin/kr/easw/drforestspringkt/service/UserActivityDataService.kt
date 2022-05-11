package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

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
        scoreMap.putIfAbsent(
            "GPS", when {
                gps <= 3 * 1000 -> 1
                gps <= 10 * 1000 -> 2
                gps <= 60 * 1000 -> 3
                gps <= 100 * 1000 -> 4
                else -> 5
            }
        )
        scoreMap.putIfAbsent(
            "OnOff", when {
                onOff <= 20 -> 1
                onOff <= 50 -> 2
                onOff <= 90 -> 3
                onOff <= 120 -> 4
                else -> 5
            }
        )

        scoreMap.putIfAbsent(
            "Step", when {
                step >= 4710 -> 1
                step >= 2355 -> 2
                step >= 942 -> 3
                step >= 471 -> 4
                else -> 5
            }
        )


        scoreMap.putIfAbsent(
            "Traffic", when {
                traffic <= 34 * 1024 * 1024 -> 1
                traffic <= 307 * 1024 * 1024 -> 2
                traffic <= 887 * 1024 * 1024 -> 3
                traffic <= 1024 * 1024 * 1024 -> 4
                else -> 5
            }
        )
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

}