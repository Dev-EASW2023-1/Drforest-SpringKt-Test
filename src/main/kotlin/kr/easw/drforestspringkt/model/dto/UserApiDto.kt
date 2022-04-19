package kr.easw.drforestspringkt.model.dto

import java.util.*


data class UserDataUploadDto(val time: Date, val data: Map<String, Float>)

data class UserDataUploadResponse(val isSuccess: Boolean)

data class UserTokenUploadDto(val token: String)

data class UploadQnADto(val title: String, val content: String)

data class UploadQnAResponse(val index: Int)


data class AcquireQnaDto(val index: Int)
data class AcquireQnaResponse(val data: QnADataDto)


data class UserNoticeRequestDto(val amount: Int, val includeIfUnread: Boolean)

data class UserNoticeResponseDto(val notice: List<UserNoticeDataDto>)

data class NoticeReadMarkRequest(val id: Int)

data class UserNoticeDataDto(
    val id: Int,
    val timestamp: Date,
    val content: String,
    val isRead: Boolean
)


data class QnAQuestionDto(val timestamp: Date, val title: String, val question: String)

data class QnaAnswerDto(val timestamp: Date, val title: String, val answer: String)

data class QnADataDto(
    val index: Int,
    val question: QnAQuestionDto,
    val answer: QnaAnswerDto?,
)