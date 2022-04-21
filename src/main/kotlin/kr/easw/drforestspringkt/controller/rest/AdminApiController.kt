package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.model.dto.RegisterDataRequest
import kr.easw.drforestspringkt.model.dto.RegisterDataResponse
import kr.easw.drforestspringkt.service.RegionManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminApiController(
    val regionManagementService: RegionManagementService
) {

    @PostMapping("/region/")
    fun onCreateRegion(@RequestParam name: String): ResponseEntity<Unit> {
        regionManagementService.registerRegion(name)
        return ResponseEntity.ok(Unit)
    }
}