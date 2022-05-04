package kr.easw.drforestspringkt.controller.web

import kr.easw.drforest.model.dto.ShareToUserRequest
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.service.SharedUserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Controller
class TemporaryWebController(val share: SharedUserService) {
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
}