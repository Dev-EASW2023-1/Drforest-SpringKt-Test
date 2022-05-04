package kr.easw.drforestspringkt.service

import kr.easw.drforest.model.dto.UserNoticeDataDto
import kr.easw.drforest.model.dto.UserNoticeRequestDto
import kr.easw.drforest.model.dto.UserNoticeResponseDto
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.AnnouncementDto
import kr.easw.drforestspringkt.model.dto.AnnouncementResponse
import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import kr.easw.drforestspringkt.model.repository.AnnouncementRepository
import kr.easw.drforestspringkt.model.repository.UserNoticeRepository
import kr.easw.drforestspringkt.util.FCMUtility
import org.springframework.data.domain.PageRequest
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class UserNoticeService(
    val repo: UserNoticeRepository
) {
    fun getNotice(user: UserAccountData, noticeRequestDto: UserNoticeRequestDto): UserNoticeResponseDto {
        return UserNoticeResponseDto(repo.getAllByUser_Account_UserId(
            user.username,
            PageRequest.of(0, noticeRequestDto.amount)
        ).map { x -> UserNoticeDataDto(x.id.toInt(), x.time!!, x.content, x.content, x.isRead) }.run {
            if (!noticeRequestDto.includeIfUnread)
                filter { x -> x.isRead }
            else this
        })

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

}

