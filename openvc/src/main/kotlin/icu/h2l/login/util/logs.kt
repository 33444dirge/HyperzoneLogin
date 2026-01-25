package icu.h2l.login.util

import icu.h2l.login.HyperZoneLoginMain

private const val DEBUG_MESSAGE_PREFIX = "[DEBUG] "

internal fun info(block: () -> String) {
    val logger = HyperZoneLoginMain.getInstance().logger
    if (logger.isInfoEnabled) {
        logger.info(block())
    }
}

internal fun debug(block: () -> String) {
    info { "$DEBUG_MESSAGE_PREFIX${block()}" }
}