package kr.easw.drforestspringkt.controller.rest

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.service.AnnouncementService
import kr.easw.drforestspringkt.service.RegionManagementService
import kr.easw.drforestspringkt.service.UserNoticeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@ApiResponse(responseCode = "403", description = "관리자 토큰이 아닌 토큰으로 접근할 경우, 권한 부족 오류가 발생합니다.")
class AdminApiController(
    val regionManagementService: RegionManagementService,
    val noticeService: UserNoticeService,
    val announcementService: AnnouncementService
) {

    @PutMapping("/region/")
    @Tag(name = "지역 추가 API", description = "시스템에 지역을 추가합니다.")
    fun onCreateRegion(@RequestBody request: AddRegionRequest): ResponseEntity<AddRegionResponse> {
        return ResponseEntity.ok(regionManagementService.registerRegion(request))
    }

    @PutMapping("/notice/{user}/")
    @Tag(name = "유저 개인 알림 추가 API", description = "대상 유저에게 알림을 추가합니다.\n파라미터의 Boolean 값으로 기기에 알림을 보낼지의 여부를 지정할 수 있습니다.")
    fun onCreateUserNotice(
        @PathVariable("user") user: String,
        @RequestBody request: AddUserNoticeRequest
    ): ResponseEntity<AddUserNoticeResponse> {
        return ResponseEntity.ok(noticeService.addNotice(user, request))
    }


    @PutMapping("/announcement/")
    @Tag(name = "서버 공지 추가 API", description = "서버에 공지사항을 추가합니다.")
    fun onCreateAnnouncement(@RequestBody request: AddAnnouncementRequest): ResponseEntity<AddAnnouncementResponse> {
        return ResponseEntity.ok(announcementService.addAnnouncement(request))
    }

    @GetMapping("/announcement/")
    @Tag(name = "서버 공지 목록 API", description = "서버에 등록된 모든 공지사항을 추가합니다.\n** 경고 ** 해당 API는 추후 Paging 형식으로 변경될 수 있습니다.")
    fun onRequestAnnouncementList() : ResponseEntity<ListAnnouncementResponse> {
        return ResponseEntity.ok(
            ListAnnouncementResponse(
                announcementService.getAllAnnouncement().announcement.values.map {
                    AnnouncementEntityData(it.id, it.time, it.title, it.content)
                }
            )
        )
    }
}