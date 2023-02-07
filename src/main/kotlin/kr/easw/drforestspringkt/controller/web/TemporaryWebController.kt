package kr.easw.drforestspringkt.controller.web

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.enumeration.Roles
import kr.easw.drforestspringkt.model.dto.AddRegionResponse
import kr.easw.drforestspringkt.model.dto.ShareToUserRequest
import kr.easw.drforestspringkt.service.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Controller
class TemporaryWebController(
    val userService: AuthenticateService,
    val share: SharedUserService,
    val noticeService: UserNoticeService,
    val announcementService: AnnouncementService,
    val regionManagementService: RegionManagementService,
    val dailyScoreLogService: DailyScoreLogService
) {
    @GetMapping("/regions")
    fun addRegions() = "init/add_region.html"

    @GetMapping("/add-dummy/")
    fun addDummyData(@RequestParam("user") user: String): String {
        return "temp/request_complete.html"
    }


    @GetMapping("/share")
    fun shareTemp(): String {
        return "init/share_to_user.html"
    }


    @GetMapping("/notice")
    fun noticeTemp(): String {
        return "init/add_notification.html"
    }


    @GetMapping("/announcement")
    fun announcementTemp(): String {
        return "init/add_announcement.html"
    }

    @GetMapping("/permissions")
    fun permissionsTemp(): String {
        return "temp/recalc_permission.html"
    }


    @GetMapping("/import-daily")
    fun importDailyTemp(): String {
        return "temp/instant_log_now.html"
    }

    @PostMapping("/log-daily-data-now/")
    fun addNow() : String {
        dailyScoreLogService.logScoreNow()
        return "redirect:/import-daily?msg=${
            URLEncoder.encode(
                "추가되었습니다.", StandardCharsets.UTF_8
            )
        }"
    }

    @PostMapping("/add-share-temp")
    fun addShare(
        @RequestParam("name") user: String,
        @RequestParam("target") target: String,
        @RequestParam("doShare") isShared: Boolean
    ): String {
        return "redirect:/share?msg=${
            URLEncoder.encode(
                share.addShare(
                    UserAccountData(
                        user, "", false, accountNonExpired = false,
                        credentialsNonExpired = false, accountNonLocked = false
                    ),
                    ShareToUserRequest(target),
                    isShared
                ).message, StandardCharsets.UTF_8
            )
        }"
    }

    @PostMapping("/add-notification-temp")
    fun addNotification(
        @RequestParam("name") user: String,
        @RequestParam("notice") notice: String,
    ): String {
        return "redirect:/notice?msg=${
            URLEncoder.encode(
                noticeService.addNotice(
                    UserAccountData(
                        user, "", false, accountNonExpired = false,
                        credentialsNonExpired = false, accountNonLocked = false
                    ),
                    notice
                ), StandardCharsets.UTF_8
            )
        }"
    }

    @PostMapping("/add-announcement-temp")
    fun addAnnouncement(
        @RequestParam("title") title: String,
        @RequestParam("contents") contents: String,
    ): String {
        announcementService.addAnnouncement("관리자", null, title, contents)
        return "redirect:/announcement?msg=${
            URLEncoder.encode(
                "공지사항이 추가되었습니다.", StandardCharsets.UTF_8
            )
        }"
    }

    @PostMapping("/change-permission") // 처음 환경 세팅 때 필요, 권한 없이 접근할 수 있는 Admin 권한 부여 API
    fun onChangePermission(
        @RequestParam("user") user: String,
        @RequestParam("admin") admin: Boolean,
    ): String {
        userService.addPermission(user, Roles.USER)
        if (admin)
            userService.addPermission(user, Roles.ADMIN)
        return "redirect:/permissions?msg=${
            URLEncoder.encode(
                "권한이 변경되었습니다.", StandardCharsets.UTF_8
            )
        }"
    }

    @PostMapping("/region") // 처음 환경 세팅 때 필요, 권한 없이 접근할 수 있는 지역 추가 API
    fun onCreateRegion(@RequestParam regionName: String): ResponseEntity<AddRegionResponse> {
        return ResponseEntity.ok(AddRegionResponse(regionManagementService.registerRegion(regionName)))
    }

}