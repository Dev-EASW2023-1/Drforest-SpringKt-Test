package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.service.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserApiController(
    val authenticateService: AuthenticateService,
    val userDataService: UserActivityDataService,
    val notificationService: UserNoticeService,
    val sharedUserService: SharedUserService,
    val qnaManagementService: QnAManagementService,
) {
    @PutMapping("/upload")
    fun onUploadData(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: UserDataUploadRequest,
    ): ResponseEntity<UserDataUploadResponse> {
        userDataService.upload(user, dto)
        return ResponseEntity.ok(UserDataUploadResponse(true))
    }


    @PutMapping("/token")
    fun onUploadToken(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: UserDataUploadRequest,
    ): ResponseEntity<UserDataUploadResponse> {
        userDataService.upload(user, dto)
        return ResponseEntity.ok(UserDataUploadResponse(true))
    }

    @GetMapping("/data")
    fun onGetUserData(
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<UserDataDto> {
        return ResponseEntity.ok(authenticateService.getUserData(user))
    }


    @GetMapping("/notice/{amount}")
    fun onGetUserNotice(
        @AuthenticationPrincipal user: UserAccountData,
        @PathVariable("amount") amount: Int
    ): ResponseEntity<UserNoticeResponseDto> {
        return ResponseEntity.ok(notificationService.getNotice(user, amount))
    }

    @PutMapping("/notice/read")
    fun onUserNoticeRead(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: NoticeReadMarkRequest
    ): ResponseEntity<Void> {
        notificationService.markRead(user, req.id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/QnA/")
    fun onRequestQnAList(@AuthenticationPrincipal user: UserAccountData): QnADataResponse {
        return qnaManagementService.findQnA(user)
    }

    @GetMapping("/share/")
    fun onRequestSharedList(@AuthenticationPrincipal user: UserAccountData): PendingUserListResponse {
        return sharedUserService.findAllPendingUser(user)
    }


    @GetMapping("/share/pending")
    fun onRequestPendingList(@AuthenticationPrincipal user: UserAccountData): SharedUserListResponse {
        return sharedUserService.findAllSharedUser(user)
    }

    @PutMapping("/share/")
    fun onRequestShareToUser(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: ShareToUserRequest
    ): ShareToUserResponse {
        return sharedUserService.addShare(user, req)
    }


    @PutMapping("/share/accept")
    fun onAcceptSharing(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: AcceptShareRequest
    ): AcceptShareResponse {
        return sharedUserService.acceptShare(user, req)
    }


    @GetMapping("/list/")
    fun onRequestListUser(
        @AuthenticationPrincipal user: UserAccountData
    ): ListUserResponse {
        return sharedUserService.getAllUsers(user)
    }


    @PatchMapping("/user/")
    fun onChangeUserDAta(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: ChangeUserDataRequest
    ): ChangeUserDataResponse {
        return TODO()
    }


    @GetMapping("/score/")
    fun onRequestSelfScore(
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<UserStatusResponse> {
        return ResponseEntity.ok(UserStatusResponse(userDataService.calculateTodayScore(user.username)))
    }


    @GetMapping("/score/share")
    fun onRequestSharedScore(
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<SharedUserScoreResponse> {
        return ResponseEntity.ok(userDataService.fetchSharedScore(user))
    }


}