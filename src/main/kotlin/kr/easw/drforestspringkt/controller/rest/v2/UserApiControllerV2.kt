package kr.easw.drforestspringkt.controller.rest.v2

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.BulkUserDataUploadRequest
import kr.easw.drforestspringkt.model.dto.BulkUserDataUploadResponse
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
class UserApiControllerV2(
    val authenticateService: AuthenticateService,
    val userDataService: UserActivityDataService,
) {
    @PutMapping("/upload")
    fun onUploadData(
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody dto: BulkUserDataUploadRequest,
    ): ResponseEntity<BulkUserDataUploadResponse> {
        userDataService.upload(user, dto)
        return ResponseEntity.ok(BulkUserDataUploadResponse(true))
    }
}