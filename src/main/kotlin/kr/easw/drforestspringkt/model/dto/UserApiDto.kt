package kr.easw.drforest.model.dto

import java.util.*


data class UserDataUploadDto(val time: Date, val data: Map<String, Float>)

data class UserDataUploadResponse(val isSuccess: Boolean)

data class UploadQnADto(val title: String, val content: String)

data class UploadQnAResponse(val index: Int)


data class AcquireQnaDto(val index: Int)

data class AcquireQnaResponse(
    val question: QnAQuestionDto,
    val answer: QnaAnswerDto?,
)

data class UserActivityDataDto(
    val time: Date,
    val value: Map<String, Float>
)

data class UserActivityContainerDto(
    val list : List<UserActivityDataDto>
)

data class QnADataDto(val index: Int, val question: QnAQuestionDto, val answer: QnaAnswerDto?)
data class UserNoticeRequestDto(val amount: Int, val includeIfUnread: Boolean)

data class UserNoticeResponseDto(val notice: List<UserNoticeDataDto>)

data class NoticeReadMarkRequest(val id: Int)

data class UserNoticeDataDto(
    val id: Int,
    val timestamp: Date,
    val title: String,
    val content: String,
    val isRead: Boolean
)


data class QnAQuestionDto(val timestamp: Date, val title: String, val question: String)

data class QnaAnswerDto(val timestamp: Date, val title: String, val answer: String)

data class SharedUserListResponse(val users: List<SharedUserData>)

data class SharedUserData(val userId: String, val userName: String, val isShared: Boolean)

data class ShareToUserRequest(val userId: String)

data class ShareToUserResponse(val userId: String, val success: Boolean, val message: String)

data class ChangeUserDataRequest(val userName: String, val beforePassword: String, val changedPassword: String)

data class ChangeUserDataResponse(val userName: String, val msg: String)