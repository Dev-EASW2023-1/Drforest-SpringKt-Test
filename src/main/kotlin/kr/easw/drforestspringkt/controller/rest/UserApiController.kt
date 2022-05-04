package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforest.model.dto.*
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.UserDataDto
import kr.easw.drforestspringkt.service.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.thymeleaf.model.IAttribute

@RestController
@RequestMapping("/api/user")
class UserApiController(
    val authenticateService: AuthenticateService,
    val userDataService: UserActivityDataService,
    val notificationService: UserNoticeService,
    val sharedUserService: SharedUserService
) {
    @PutMapping("/upload")
    fun onUploadData(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: UserDataUploadDto,
    ): ResponseEntity<UserDataUploadResponse> {
        userDataService.upload(user, dto)
        return ResponseEntity.ok(UserDataUploadResponse(true))
    }


    @PutMapping("/token")
    fun onUploadToken(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: UserDataUploadDto,
    ): ResponseEntity<UserDataUploadResponse> {
        userDataService.upload(user, dto)
        return ResponseEntity.ok(UserDataUploadResponse(true))
    }

    @GetMapping("/data")
    fun onGetUserData(
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<UserDataDto> {
        println("Data get")
        println(authenticateService.getUserData(user))
        return ResponseEntity.ok(authenticateService.getUserData(user))
    }


    @GetMapping("/notice")
    fun onGetUserNotice(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: UserNoticeRequestDto
    ): ResponseEntity<UserNoticeResponseDto> {
        return ResponseEntity.ok(notificationService.getNotice(user, req))
    }

    @PutMapping("/notice/read")
    fun onUserNoticeRead(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: NoticeReadMarkRequest
    ): ResponseEntity<Void> {
        notificationService.markRead(user, req.id)
        return ResponseEntity.ok().build()
    }


    @GetMapping("/share/")
    fun onRequestSharedList(@AuthenticationPrincipal user: UserAccountData): SharedUserListResponse {
        return sharedUserService.findAllUser(user)
    }

    @PutMapping("/share/")
    fun onRequestShareToUser(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: ShareToUserRequest
    ): ShareToUserResponse {
        return sharedUserService.addShare(user, req)
    }


    @PatchMapping("/user/")
    fun onChangeUserDAta(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody req: ChangeUserDataRequest
    ): ChangeUserDataResponse {
        return TODO()
    }


}