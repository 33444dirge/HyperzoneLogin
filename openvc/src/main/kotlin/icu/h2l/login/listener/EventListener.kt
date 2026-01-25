package icu.h2l.login.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.util.GameProfile
import icu.h2l.login.HyperZoneLoginMain
import icu.h2l.login.type.OfflineUUIDType
import icu.h2l.login.util.ExtraUuidUtils
import `fun`.iiii.openvelocity.api.event.connection.OnlineAuthEvent
import `fun`.iiii.openvelocity.api.event.connection.OpenPreLoginEvent
import icu.h2l.login.util.info

class EventListener {
    @Subscribe
    fun onPreLogin(event: OpenPreLoginEvent) {
        val uuid = event.uuid
        val name = event.userName
        val host = event.host
        val offlineUUIDType = ExtraUuidUtils.matchType(uuid, name)

        val offlineHost = HyperZoneLoginMain.getInstance().loginServerManager.shouldOfflineHost(host)
        if (offlineHost) {
            info { "匹配到离线 host 玩家: $name" }
        }
        if (offlineUUIDType != OfflineUUIDType.UNKNOWN || offlineHost) {
            event.isOnline = false
        } else {
            event.isOnline = true
        }
        info { "传入 UUID 信息玩家: $name UUID:$uuid 类型: $offlineUUIDType 在线:${event.isOnline}" }
    }

    @Subscribe
    fun onPreLogin(event: OnlineAuthEvent) {
//        测试
        info { "已跳过登入" }
        event.isSuccess = true
        event.gameProfile= GameProfile.forOfflinePlayer(event.userName)
    }
} 