package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforest.model.dto.UserDataUploadDto
import kr.easw.drforest.model.dto.UserDataUploadResponse
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.service.UserActivityDataService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserApiController(
    val service: UserActivityDataService,
) {
    @PutMapping("/upload")
    fun onUploadData(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: UserDataUploadDto,
    ): ResponseEntity<UserDataUploadResponse> {
        service.upload(user, dto)
        return ResponseEntity.ok(UserDataUploadResponse(true))
    }
}