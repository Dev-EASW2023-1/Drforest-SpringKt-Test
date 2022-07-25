package kr.easw.drforestspringkt.controller.rest.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.service.AnnouncementService
import kr.easw.drforestspringkt.service.AuthenticateService
import kr.easw.drforestspringkt.service.MemberManagementService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/manager")
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "요청한 작업이 성공할 때 해당 코드가 반환됩니다.\n해당 코드는 예측된 오류가 발생하였을 경우에도 발생하며, 이 경우에는 반환값의 isSuccess 필드를 이용해 분류합니다."
    ),
    ApiResponse(responseCode = "403", description = "관리자 토큰이 아닌 토큰으로 접근할 경우, 권한 부족으로 판단되어 해당 코드가 반환됩니다."),
    ApiResponse(responseCode = "500", description = "서버에서 예상치 못한 오류가 발생했을 경우, 해당 코드가 반환됩니다."),
)
class ManagerApiController(
    val announcementService: AnnouncementService,
    val userService: AuthenticateService,
    val memberManagementService: MemberManagementService
) {

    @DeleteMapping
    @Tag(name = "사용자 API", description = "사용자를 관리합니다.")
    @Operation(summary = "사용자 삭제 API", security = [SecurityRequirement(name = "JWT")])
    fun onDeleteUser(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody request: DeleteUserRequest) {
        memberManagementService.deleteUser(user, request)
    }

    @PutMapping("/announcement/")
    @Tag(
        name = "지역 공지사항 API", description = "지역에 등록된 공지사항을 관리합니다."
    )
    @Operation(
        summary = "지역 공지사항 추가 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onCreateAnnouncementForRegion(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody request: AddAnnouncementRequest
    ): ResponseEntity<AddAnnouncementResponse> {
        return ResponseEntity.ok(announcementService.addAnnouncement(user, request))
    }

    @GetMapping("/announcement/")
    @Tag(
        name = "지역 공지사항 API", description = "지역에 등록된 공지사항을 관리합니다."
    )
    @Operation(
        summary = "공지사항 목록 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onRequestAnnouncementList(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: UserAccountData
    ): ResponseEntity<ListAnnouncementResponse> {
        return ResponseEntity.ok(
            ListAnnouncementResponse(
                announcementService.getAnnouncementOfUserRegion(user).announcement.values.map {
                    AnnouncementEntityData(it.id, it.time, it.title, it.content)
                }
            )
        )
    }

    @DeleteMapping("/announcement/{announcementId}")
    @Tag(
        name = "지역 공지사항 API", description = "지역에 등록된 공지사항을 관리합니다." +
                "** 경고 ** GET API는 추후 Paging 형식으로 변경될 수 있습니다."
    )
    @Operation(
        summary = "공지사항 삭제 API",
        security = [SecurityRequirement(name = "JWT")]
    )
    fun onDeleteAnnouncement(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: UserAccountData,
        @RequestBody request: AddAnnouncementRequest,
        @PathVariable announcementId: Long
    ): ResponseEntity<DeleteAnnouncementResponse> {
        return ResponseEntity.ok(
            DeleteAnnouncementResponse(
                announcementService.deleteAnnouncement(
                    user,
                    announcementId
                )
            )
        )
    }

}