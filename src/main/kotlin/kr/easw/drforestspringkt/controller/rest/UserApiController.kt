package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.model.dto.UserDataUploadDto
import kr.easw.drforestspringkt.model.dto.UserDataUploadResponse
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.service.UserActivityDataService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserApiController(
    val userDataService: UserActivityDataService,
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
}