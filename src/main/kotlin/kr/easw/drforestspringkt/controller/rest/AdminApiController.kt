package kr.easw.drforestspringkt.controller.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.service.AnnouncementService
import kr.easw.drforestspringkt.service.RegionManagementService
import kr.easw.drforestspringkt.service.UserNoticeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@ApiResponses(
    ApiResponse(responseCode = "200", description = "요청한 작업이 성공할 때 해당 코드가 반환됩니다.\n해당 코드는 예측된 오류가 발생하였을 경우에도 발생하며, 이 경우에는 반환값의 isSuccess 필드를 이용해 분류합니다."),
    ApiResponse(responseCode = "403", description = "관리자 토큰이 아닌 토큰으로 접근할 경우, 권한 부족으로 판단되어 해당 코드가 반환됩니다."),
    ApiResponse(responseCode = "500", description = "서버에서 예상치 못한 오류가 발생했을 경우, 해당 코드가 반환됩니다."),
)
class AdminApiController(
    val regionManagementService: RegionManagementService,
    val noticeService: UserNoticeService,
    val announcementService: AnnouncementService
) {

    @PutMapping("/region/{regionName}")
    @Tag(name = "지역 API", description = "시스템에 등록된 지역을 관리합니다.")
    @Operation(
        summary = "지역 생성 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onCreateRegion(@PathVariable("regionName") regionName: String): ResponseEntity<AddRegionResponse> {
        return ResponseEntity.ok(AddRegionResponse(regionManagementService.registerRegion(regionName)))
    }


    @DeleteMapping("/region/{regionName}")
    @Tag(name = "지역 API", description = "시스템에 등록된 지역을 관리합니다.")
    @Operation(
        summary = "관리자 삭제 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onDeleteRegion(@PathVariable("regionName") regionName: String): ResponseEntity<DeleteRegionResponse> {
        return ResponseEntity.ok(DeleteRegionResponse(regionManagementService.deleteRegion(regionName)))
    }

    @PutMapping("/notice/{user}/")
    @Tag(name = "유저 개인 알림 API", description = "유저의 개인 알림 정보를 관리합니다.")
    @Operation(
        summary = "유저 개인 알림 추가 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onCreateUserNotice(
        @PathVariable("user") user: String,
        @RequestBody request: AddUserNoticeRequest
    ): ResponseEntity<AddUserNoticeResponse> {
        return ResponseEntity.ok(noticeService.addNotice(user, request))
    }


    @PutMapping("/announcement/")
    @Tag(
        name = "서버 공지사항 API", description = "서버에 등록된 공지사항을 관리합니다."
    )
    @Operation(
        summary = "관리자 ID 토큰",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onCreateAnnouncement(@RequestBody request: AddGlobalAnnouncementRequest): ResponseEntity<AddAnnouncementResponse> {
        return ResponseEntity.ok(announcementService.addAnnouncement(request))
    }

    @PutMapping("/announcement/{regionName}")
    @Tag(
        name = "서버 API", description = "서버에 등록된 공지사항을 관리합니다."
    )
    @Operation(
        summary = "지역 공지사항 추가 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onCreateAnnouncementForRegion(
        @RequestBody request: AddAnnouncementRequest
    ): ResponseEntity<AddAnnouncementResponse> {
        return ResponseEntity.ok(announcementService.addAnnouncement(request))
    }

    @GetMapping("/announcement/")
    @Tag(
        name = "서버 API", description = "서버에 등록된 공지사항을 관리합니다."
    )
    @Operation(
        summary = "공지사항 목록 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onRequestAnnouncementList(): ResponseEntity<ListAnnouncementResponse> {
        return ResponseEntity.ok(
            ListAnnouncementResponse(
                announcementService.getAllAnnouncement().announcement.values.map {
                    AnnouncementEntityData(it.id, it.time, it.title, it.content)
                }
            )
        )
    }

    @DeleteMapping("/announcement/{announcementId}")
    @Tag(
        name = "서버 API", description = "서버에 등록된 공지사항을 관리합니다." +
                "** 경고 ** GET API는 추후 Paging 형식으로 변경될 수 있습니다."
    )
    @Operation(
        summary = "공지사항 삭제 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onDeleteAnnouncement(
        @RequestBody request: AddAnnouncementRequest,
        @PathVariable announcementId: Long
    ): ResponseEntity<DeleteAnnouncementResponse> {
        return ResponseEntity.ok(DeleteAnnouncementResponse(announcementService.deleteAnnouncement(announcementId)))
    }
}