package kr.easw.drforestspringkt.controller.web

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.service.UserActivityDataService
import kr.easw.drforestspringkt.service.UserDataService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.ZoneId
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/user")
class UserWebController(val userActivityDataService: UserActivityDataService, val userDataService: UserDataService) {

    @GetMapping("/graph")
    fun onRequestGraph(
        @AuthenticationPrincipal user: UserAccountData, model: Model
    ): String {
        val time = 1000L * 60 * 60 * 24 * 5
        val endTime = System.currentTimeMillis()
        model.addAttribute("start", endTime - time)
        model.addAttribute("end", endTime)
        model.addAttribute("graph", userActivityDataService.fetchResult(user.username, time))
        return "user/graph.html"
    }

    // @TODO 테스트를 위한 그래프 페이지, 이후에 제거 필요
    @GetMapping("/graph2")
    fun graphPage2(
        @RequestParam("timeZone") timeZone: ZoneId?,
        @RequestParam("userId") userId: String,
        model: Model,
        response: HttpServletResponse
    ): String? {
        val time = 1000L * 60 * 60 * 24 * 5
        val endTime = System.currentTimeMillis()
        model.addAttribute("start", (endTime - time) - (endTime - time) % (15 * 60 * 1000))
        model.addAttribute("end", endTime - endTime % (15 * 60 * 1000))
        model.addAttribute("graph", userActivityDataService.fetchResult(userId, time))
        return "user/graph.html"
    }


}