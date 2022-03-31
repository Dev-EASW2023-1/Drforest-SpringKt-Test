package kr.easw.drforestspringkt.controller.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminWebController {
    @GetMapping("/admin/panel/")
    fun adminPanel() = "/admin/panel.html"

    @GetMapping("/admin/panel/list-user")
    fun listUser() = "/admin/panel.html"

    @GetMapping("/admin/panel/user-status")
    fun userStatus() = "/admin/panel.html"

}