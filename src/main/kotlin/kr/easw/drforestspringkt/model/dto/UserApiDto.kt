package kr.easw.drforestspringkt.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*


data class UserDataUploadRequest(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val time: Date, val data: Map<String, Float>
)

data class UserDataUploadResponse(val isSuccess: Boolean)

data class BulkUserDataUploadRequest(
    val requests: List<UserDataUploadRequest>
)

data class BulkUserDataUploadResponse(val isSuccess: Boolean)

data class UserStatusResponse(val data: UserScoreData)

data class SharedUserScoreResponse(val data: List<UserScoreData>)

data class PendingUserListResponse(val data: List<SharedUserData>)

data class UploadQnARequest(val title: String, val content: String)

data class UploadQnAResponse(val index: Int)


data class AcquireQnaRequest(val index: Int)

data class AcquireQnaResponse(
    val question: QnAQuestionData,
    val answer: QnaAnswerData?,
)


data class UserNoticeResponseDto(val notice: List<UserNoticeData>)

data class NoticeReadMarkRequest(val id: Int)

data class SharableUserListResponse(val users: List<SharableUserData>)

data class SharedUserListResponse(val users: List<SharedUserData>)

data class ShareToUserRequest(val userId: String)

data class ShareToUserWithPhoneNumberRequest(val phoneNumber: String)

data class ShareToUserWithPhoneNumberResponse(
    val isSuccess: Boolean,
    val userId: String,
    val message: String
)

data class AcceptShareRequest(val userId: String)

data class AcceptShareResponse(val isSuccess: Boolean, val userId: String, val message: String)

data class CancelShareRequest(val userId: String)

data class CancelShareResponse(val isSuccess: Boolean, val userId: String, val message: String)

data class CancelShareRequestRequest(val userId: String)

data class CancelShareRequestResponse(
    val isSuccess: Boolean,
    val userId: String,
    val message: String
)

data class ShareToUserResponse(val userId: String, val success: Boolean, val message: String)

data class ChangeUserDataRequest(
    val userName: String,
    val beforePassword: String,
    val changedPassword: String
)

data class ChangeUserDataResponse(val userName: String, val msg: String)

data class ListUserResponse(val users: List<SharedUserData>)


data class UserNoticeData(
    val id: Int,
    val timestamp: Date,
    val content: String,
    val isRead: Boolean
)

data class UserSummaryResponse(val scores: Map<String, Int>, val data: UserCreatedTimeDataDto)

data class SharedUserData(
    val userId: String,
    val userName: String,
    val phoneNumber: String?,
    val isShared: Boolean
)

// ShareStatus
// 0 - Not shared
// 1 - Share request pending
// 2 - Shared
data class SharableUserData(
    val userId: String,
    val userName: String,
    val phoneNumber: String?,
    val shareStatus: Int
)


data class QnAQuestionData(val timestamp: Date, val title: String, val question: String)

data class QnaAnswerData(val timestamp: Date, val title: String, val answer: String)


data class UserActivityContainerData(
    val list: List<UserActivityDataData>
)

data class UserActivityDataData(
    val time: Date,
    val value: Map<String, Float>
)

data class QnADataData(val index: Int, val question: QnAQuestionData, val answer: QnaAnswerData?)

data class QnADataResponse(val qna: List<QnADataData>)

data class UserScoreData(var name: String, val score: MutableMap<String, Int>)