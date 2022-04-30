package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.model.dto.UserDataUploadDto
import kr.easw.drforestspringkt.model.dto.UserDataUploadResponse
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.UserDataDto
import kr.easw.drforestspringkt.service.AnnouncementService
import kr.easw.drforestspringkt.service.AuthenticateService
import kr.easw.drforestspringkt.service.UserActivityDataService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
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
    val notificationService: AnnouncementService,
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
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<UserDataDto> {
        println("Data get")
        println(authenticateService.getUserData(user))
        return ResponseEntity.ok(authenticateService.getUserData(user))
    }

    @PutMapping("/notice/read")
    fun onUserNoticeRead(
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<UserDataDto> {
        println("Data put")
        println(authenticateService.getUserData(user))
        return ResponseEntity.ok(authenticateService.getUserData(user))
    }


}