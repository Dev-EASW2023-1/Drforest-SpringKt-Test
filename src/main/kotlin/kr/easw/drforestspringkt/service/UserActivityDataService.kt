package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.UserActivityContainerData
import kr.easw.drforestspringkt.model.dto.UserActivityDataData
import kr.easw.drforestspringkt.model.dto.UserDataUploadRequest
import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserActivityDataService(val authService: AuthenticateService, val repo: UserActivityDataRepository) {
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


}