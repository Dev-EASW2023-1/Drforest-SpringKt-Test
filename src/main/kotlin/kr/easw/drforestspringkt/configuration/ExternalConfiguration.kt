package kr.easw.drforestspringkt.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "drforest")
class ExternalConfiguration {
    var sqlAddress = "http://"
    var sqlUser = "root"
    var sqlPassword = "1111"
}