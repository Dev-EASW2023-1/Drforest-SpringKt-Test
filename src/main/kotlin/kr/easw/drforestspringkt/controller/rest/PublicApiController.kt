package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.model.dto.CheckAccountDuplicateDto
import kr.easw.drforestspringkt.model.dto.CheckTokenValidDto
import kr.easw.drforestspringkt.model.dto.CheckTokenValidResponse
import kr.easw.drforestspringkt.model.dto.ListRegionResponse
import kr.easw.drforestspringkt.service.AuthenticateService
import kr.easw.drforestspringkt.service.RegionManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public")
class PublicApiController(
    private val authenticateService: AuthenticateService,
    private val regionManagementService: RegionManagementService,
) {
    @PostMapping("/account-exists")
    fun onCheckDuplicate(@RequestBody dto: CheckAccountDuplicateDto): ResponseEntity<Any> {
        return ResponseEntity.ok(authenticateService.checkAccountDuplicatesResponse(dto))
    }

    @PostMapping("/list-regions")
    fun onRequestListRegions(): ResponseEntity<ListRegionResponse> {
        return ResponseEntity.ok(regionManagementService.listRegionResponse())
    }

    @PostMapping("/token-validation")
    fun onValidateToken(@RequestBody dto: CheckTokenValidDto): ResponseEntity<CheckTokenValidResponse> {
        return ResponseEntity.ok(authenticateService.checkValidationResponse(dto))
    }
}