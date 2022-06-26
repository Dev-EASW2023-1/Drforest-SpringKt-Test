package kr.easw.drforestspringkt.controller.rest

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.easw.drforestspringkt.annotations.Beta
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.service.AnnouncementService
import kr.easw.drforestspringkt.service.AuthenticateService
import kr.easw.drforestspringkt.service.RegionManagementService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public")
class PublicApiController(
    private val announcementService: AnnouncementService,
    private val authenticateService: AuthenticateService,
    private val regionManagementService: RegionManagementService,
) {

    @PostMapping("/register")
    @Hidden
    fun onCheckDuplicate(@RequestBody data: RegisterDataRequest): ResponseEntity<RegisterDataResponse> {
        return ResponseEntity.ok(authenticateService.register(data))
    }

    @GetMapping("/account-exists/{id}")
    @Hidden
    fun onCheckDuplicate(@PathVariable id: String): ResponseEntity<CheckAccountDuplicateResponse> {
        return ResponseEntity.ok(authenticateService.checkAccountDuplicatesResponse(id))
    }

    @GetMapping("/regions")
    @Hidden
    fun onRequestListRegions(): ResponseEntity<ListRegionResponse> {
        return ResponseEntity.ok(regionManagementService.listRegionResponse())
    }

    @PostMapping("/verify-token")
    @Tag(name = "인증 API", description = "ID와 PW, 혹은 토큰을 기반으로 인증을 진행합니다.")
    @Operation(summary = "토큰 유효성 확인 API")
    fun onValidateToken(@RequestBody dto: CheckTokenValidRequest): ResponseEntity<CheckTokenValidResponse> {
        return ResponseEntity.ok(authenticateService.checkValidationResponse(dto))
    }

    @PatchMapping("/change-password")
    @Hidden
    fun onChangePasswordUsingPhone(
        @RequestBody req: ChangeUserDataUsingPhoneNumberRequest
    ): ResponseEntity<ChangeUserDataResponse> {
        return ResponseEntity.ok(
            authenticateService.changePasswordUsingPhoneNumber(
                req.userName,
                req.phoneNumber!!,
                req.changedPassword
            )
        )
    }

    @PostMapping("/refresh")
    @Tag(name = "인증 API", description = "ID와 PW, 혹은 토큰을 기반으로 인증을 진행합니다.")
    @Operation(summary = "토큰 재발급 API")
    fun onRefreshToken(@RequestBody data: RefreshTokenRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(authenticateService.refreshToken(data))
    }

    @GetMapping("/announcement/")
    @Hidden
    fun getAnnouncement(@AuthenticationPrincipal user: UserAccountData): ResponseEntity<AnnouncementResponse> {
        return ResponseEntity.ok(announcementService.getUserAnnouncement(user))
    }

    @GetMapping("/announcement/{id}")
    @Hidden
    fun getAnnouncement(@PathVariable id: Long): ResponseEntity<AnnouncementData> {
        return ResponseEntity.ok(announcementService.getAnnouncement(id))
    }

    // TODO : Add user authentication
    @Beta
    @Hidden
    @GetMapping("/find/id/phone/{phoneNumber}")
    fun findIdByPhoneNumber(@PathVariable phoneNumber: String): ResponseEntity<FindUserByPhoneNumberResponse> {
        return ResponseEntity.ok(authenticateService.findUserByPhone(phoneNumber)?.run {
            FindUserByPhoneNumberResponse(true, this.account.userId)
        } ?: FindUserByPhoneNumberResponse(false, null))
    }
}