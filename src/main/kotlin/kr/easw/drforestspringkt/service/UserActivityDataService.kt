package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.dto.UserDataUploadDto
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.entity.UserActivityDataEntity
import kr.easw.drforestspringkt.model.repository.UserActivityDataRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserActivityDataService(val authService: AuthenticateService, val repo: UserActivityDataRepository) {
    @Transactional
    fun get(entity: UserAccountData) {

    }

    @Transactional
    fun upload(entity: UserAccountData, data: UserDataUploadDto) {
        val user = authService.toAccount(entity)
        data.data.forEach { (name, value) ->
            repo.save(
                UserActivityDataEntity(user, name, value)
            )
        }
    }
}