package kr.easw.drforestspringkt.util

import kr.easw.drforestspringkt.FCM_URL
import kr.easw.drforestspringkt.GOOGLE_API_KEY
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

object FCMUtility {
    private val template = RestTemplate()

    @JvmStatic
    fun main(args: Array<String>) {
        sendPush("Test1", "Test", 4)
    }

    fun sendPush(title: String, contents: String, id: Int) {
        val requestString = constructJson(
            "to" to "/topic/notice",
            "data" to constructJsonObject(
                "title" to title,
                "message" to contents,
                "id" to id
            )
        )
        template.exchange(
            FCM_URL,
            HttpMethod.POST,
            HttpEntity<String>(
                requestString,
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                    set("Authorization", "key=$GOOGLE_API_KEY")

                }
            ),
            String::class.java
        ).apply {
            logDebug(
                "Firebase FCM : Status code ${this.statusCode.name} (${this.statusCodeValue})",
                "Firebase FCM : ${this.body}"
            )
        }
    }
    // {"data":"{"id":4,"title":"Test1","message":"Test"}","to":"/topic/notice"}
}