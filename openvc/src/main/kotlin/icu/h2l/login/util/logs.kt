package icu.h2l.login.util

import icu.h2l.login.HyperZoneLoginMain

private const val DEBUG_MESSAGE_PREFIX = "[DEBUG] "

internal inline fun info(block: () -> String) {
    val logger = HyperZoneLoginMain.getInstance().logger
    if (logger.isInfoEnabled) {
        logger.info(block())
    }
}

internal inline fun debug(block: () -> String) {
    if (HyperZoneLoginMain.getConfig().advanced.debug) {
        info { "$DEBUG_MESSAGE_PREFIX${block()}" }
    }
}