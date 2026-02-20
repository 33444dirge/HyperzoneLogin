package icu.h2l.login.auth.online

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import icu.h2l.api.event.connection.OnlineAuthEvent
import icu.h2l.api.event.limbo.LimboSpawnEvent
import icu.h2l.api.log.debug

class YggdrasilEventListener(
    private val yggdrasilAuthModule: YggdrasilAuthModule
) {
    @Subscribe
    fun onOnlineAuth(event: OnlineAuthEvent) {
        if (!event.isOnline) return

        debug { "[YggdrasilFlow] OnlineAuthEvent 收到，开始验证: user=${event.userName}, uuid=${event.userUUID}" }

        yggdrasilAuthModule.startYggdrasilAuth(
            username = event.userName,
            uuid = event.userUUID,
            serverId = event.serverId,
            playerIp = event.playerIp
        )
    }

    @Subscribe
    fun onLimboSpawn(event: LimboSpawnEvent) {
        if (!event.proxyPlayer.isOnlineMode) return
        if (event.hyperZonePlayer.isVerified()) return

        val username = event.proxyPlayer.username
        debug { "[YggdrasilFlow] LimboSpawnEvent 收到，注册回调: user=$username" }
        yggdrasilAuthModule.registerLimboHandler(username, event.hyperZonePlayer)
    }

    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        val username = event.player.username
        yggdrasilAuthModule.clearPlayerCacheOnDisconnect(username)
    }
}
