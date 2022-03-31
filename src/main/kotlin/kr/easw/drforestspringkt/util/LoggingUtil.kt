package kr.easw.drforestspringkt.util

import org.slf4j.LoggerFactory



inline fun <reified T : Any> T.logDebug(vararg msg: String) {
    val logger = LoggerFactory.getLogger(this::class.java)
    for (line in msg) {
        logger.debug(line)
    }
}

inline fun <reified T : Any> T.logInfo(vararg msg: String) {
    val logger = LoggerFactory.getLogger(this::class.java)
    for (line in msg) {
        logger.info(line)
    }
}


inline fun <reified T : Any> T.logWarning(vararg msg: String) {
    val logger = LoggerFactory.getLogger(this::class.java)
    for (line in msg) {
        logger.warn(line)
    }
}


inline fun <reified T : Any> T.logError(vararg msg: String) {
    val logger = LoggerFactory.getLogger(this::class.java)
    for (line in msg) {
        logger.error(line)
    }
}