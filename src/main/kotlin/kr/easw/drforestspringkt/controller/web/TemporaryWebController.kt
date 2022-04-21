package kr.easw.drforestspringkt.controller.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TemporaryWebController {
    @GetMapping("/regions")
    fun addRegions() = "/init/add_region.html"
}