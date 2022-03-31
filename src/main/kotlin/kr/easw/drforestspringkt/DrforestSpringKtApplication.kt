package kr.easw.drforestspringkt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.util.*

@SpringBootApplication
class DrforestSpringKtApplication

fun main(args: Array<String>) {
    // 어떠한 환경에서도 작동을 보장하기 위해, 서버는 항상 UTC 기준 시각으로 연산함.
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")))
    runApplication<DrforestSpringKtApplication>(*args)
}
