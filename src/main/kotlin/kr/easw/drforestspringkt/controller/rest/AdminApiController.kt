package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.service.RegionManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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