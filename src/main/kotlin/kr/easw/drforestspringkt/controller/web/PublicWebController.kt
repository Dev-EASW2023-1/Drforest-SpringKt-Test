package kr.easw.drforestspringkt.controller.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PublicWebController {
    @GetMapping("/")
    fun index() = "/index.html"


    @GetMapping("/register")
    fun register() = "/auth/register.html"


    @GetMapping("/login")
    fun login() = "/auth/login.html"
}