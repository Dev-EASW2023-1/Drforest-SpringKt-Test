package kr.easw.drforestspringkt.controller.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TemporaryWebController() {
    @GetMapping("/regions")
    fun addRegions() = "init/add_region.html"

    @GetMapping("/add-dummy/")
    fun addDummyData(@RequestParam("user") user: String): String {

        return "temp/request_complete.html"
    }
}