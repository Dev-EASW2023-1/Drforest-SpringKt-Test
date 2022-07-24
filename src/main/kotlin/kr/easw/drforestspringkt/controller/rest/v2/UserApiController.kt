package kr.easw.drforestspringkt.controller.rest.v2

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.UserDataUploadRequest
import kr.easw.drforestspringkt.model.dto.UserDataUploadResponse
import kr.easw.drforestspringkt.service.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/user")
class UserApiController(
    val authenticateService: AuthenticateService,
    val userDataService: UserActivityDataService,
) {
    @PutMapping("/upload")
    fun onUploadData(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: List<UserDataUploadRequest>,
    ): ResponseEntity<UserDataUploadResponse> {
        userDataService.upload(user, dto)
        return ResponseEntity.ok(UserDataUploadResponse(true))
    }
}