package icu.h2l.login.manager

import icu.h2l.login.HyperZoneLoginMain

class LoginServerManager {
    fun shouldOfflineHost(hostName: String): Boolean {
        if (hostName.isEmpty()) return false
        HyperZoneLoginMain.getConfig().hostMatch.start.forEach {
            if (hostName.startsWith(it)) return true
        }
        return false
    }
} 