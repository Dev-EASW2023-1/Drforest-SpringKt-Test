package kr.easw.drforestspringkt.controller.web

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.service.UserActivityDataService
import kr.easw.drforestspringkt.service.UserDataService
import org.json.simple.JSONObject
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.time.ZoneId
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/user")
class UserWebController(val userActivityDataService: UserActivityDataService, val userDataService: UserDataService) {

    @GetMapping("/graph")
    fun onRequestGraph(
        @AuthenticationPrincipal user: UserAccountData, model: Model
    ): String {
        val time = 1000L * 60 * 60 * 24 * 2
        val timeDay = 1000L * 60 * 60 * 24 * 5
        val endTime = System.currentTimeMillis()
        model.addAttribute("start", endTime - time)
        model.addAttribute("end", endTime)
        model.addAttribute("startDay", endTime - timeDay)
        model.addAttribute("endDay", endTime)
        model.addAttribute("graph", userActivityDataService.fetchResult(user.username, time, 1000 * 60 * 15))
        model.addAttribute("graphDay", userActivityDataService.fetchResult(user.username, timeDay, 1000 * 60 * 60 * 24))
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
        val time = 1000L * 60 * 60 * 24 * 2
        val timeDay = 1000L * 60 * 60 * 24 * 5
        val endTime = System.currentTimeMillis()
        model.addAttribute("start", endTime - time)
        model.addAttribute("end", endTime)
        model.addAttribute("startDay", endTime - timeDay)
        model.addAttribute("endDay", endTime)
        model.addAttribute("graph", userActivityDataService.fetchResult(userId, time, 1000 * 60 * 15))
        model.addAttribute("graphDay", userActivityDataService.fetchResult(userId, timeDay, 1000 * 60 * 60 * 24))
        return "user/graph.html"
    }

    @RequestMapping("/paper")
    fun showGraph()
    : String? {
        return "user/monitoringGraph.html"
    }

    @GetMapping("/graph3")
    @ResponseBody
    fun graphPage3(
        @RequestParam("timeZone") timeZone: ZoneId?,
        @RequestParam("userId") userId: String,
        response: HttpServletResponse
    ): String? {
        val time = 1000L * 60 * 60 * 24 * 2
        val endTime = System.currentTimeMillis()
        val each15Minutes = endTime % (1000 * 60 * 15)
        val mapOfTime = hashMapOf<String, Long>()
        val listOfSensor = userActivityDataService.fetchResult(userId, time, 1000 * 60 * 15)
        val jsonRes = JSONObject()
        mapOfTime["start"] = endTime - time - each15Minutes
        mapOfTime["end"] = endTime - each15Minutes
        val objs = arrayOf(
            userActivityDataService.jsonParseFunction(mapOfTime),
            userActivityDataService.jsonParseFunctionForSensor("graph", listOfSensor.list)
        )

        for(obj in objs){
            val it = obj.keys.iterator()
            while (it.hasNext()) {
                val key = it.next()
                jsonRes[key] = obj[key]
            }
        }

       return jsonRes.toJSONString()
    }
}