package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.UserNoticeData
import kr.easw.drforestspringkt.model.dto.UserNoticeResponseDto
import kr.easw.drforestspringkt.model.entity.UserNoticeEntity
import kr.easw.drforestspringkt.model.repository.UserNoticeRepository
import kr.easw.drforestspringkt.util.FCMUtility
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class UserNoticeService(
    val repo: UserNoticeRepository,
    val authenticateService: AuthenticateService,
) {
    fun getNotice(user: UserAccountData, amount: Int): UserNoticeResponseDto {
        return UserNoticeResponseDto(repo.getAllByUser_Account_UserId(
            user.username,
            PageRequest.of(0, amount)
        ).map { x -> UserNoticeData(x.id.toInt(), x.time!!, x.content, x.isRead) })
    }

    fun markRead(user: UserAccountData, id: Int) {
        val data = try {
            repo.getById(id.toLong())
        } catch (e: Throwable) {
            return
        }
        if (data.user.account.userId != user.username) {
            // Permission denied; Do not throw exception to hide exception at client
            return
        }
        data.isRead = true
        repo.save(data)
    }

    fun addNotice(userAccountData: UserAccountData, notice: String): String {
        val user = authenticateService.findAccountByUserId(userAccountData.username) ?: return "등록된 유저가 아닙니다."
        repo.save(
            UserNoticeEntity(
                user,
                notice,
                false
            )
        )
        return "알림을 추가하였습니다."
    }

}

