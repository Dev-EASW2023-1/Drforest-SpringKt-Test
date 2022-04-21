package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.service.AnnouncementService
import kr.easw.drforestspringkt.service.AuthenticateService
import kr.easw.drforestspringkt.service.RegionManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public")
class PublicApiController(
    private val announcementService: AnnouncementService,
    private val authenticateService: AuthenticateService,
    private val regionManagementService: RegionManagementService,
    ) {

    @PostMapping("/register")
    fun onCheckDuplicate(@RequestBody data : RegisterDataRequest): ResponseEntity<RegisterDataResponse> {
        return ResponseEntity.ok(authenticateService.register(data))
    }

    @GetMapping("/account-exists/{id}")
    fun onCheckDuplicate(@PathVariable id: String): ResponseEntity<CheckAccountDuplicateResponse> {
        return ResponseEntity.ok(authenticateService.checkAccountDuplicatesResponse(id))
    }

    @GetMapping("/regions")
    fun onRequestListRegions(): ResponseEntity<ListRegionResponse> {
        return ResponseEntity.ok(regionManagementService.listRegionResponse())
    }

    @PostMapping("/verify-token")
    fun onValidateToken(@RequestBody dto: CheckTokenValidDto): ResponseEntity<CheckTokenValidResponse> {
        return ResponseEntity.ok(authenticateService.checkValidationResponse(dto))
    }


    @PostMapping("/refresh")
    fun onRefreshToken(@RequestBody data: RefreshTokenDto): ResponseEntity<Any> {
        return ResponseEntity.ok(authenticateService.refreshToken(data))
    }

    @GetMapping("/announcement/")
    fun getAnnouncement(): ResponseEntity<AnnouncementResponse> {
        return ResponseEntity.ok(announcementService.getAllAnnouncement())
    }

    @GetMapping("/announcement/{id}")
    fun getAnnouncement(@PathVariable id: Long): ResponseEntity<AnnouncementDto> {
        return ResponseEntity.ok(announcementService.getAnnouncement(id))
    }
}