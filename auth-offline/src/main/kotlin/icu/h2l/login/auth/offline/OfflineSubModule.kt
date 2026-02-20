package icu.h2l.login.auth.offline

import com.velocitypowered.api.proxy.ProxyServer
import icu.h2l.api.db.HyperZoneDatabaseManager
import icu.h2l.api.limbo.HyperZoneLimboProvider
import icu.h2l.api.module.HyperSubModule
import icu.h2l.api.player.HyperZonePlayerAccessorProvider
import icu.h2l.login.auth.offline.command.OfflineAuthCommandRegistrar
import java.nio.file.Path
import java.util.logging.Logger

class OfflineSubModule : HyperSubModule {
    override fun register(
        owner: Any,
        proxy: ProxyServer,
        dataDirectory: Path,
        databaseManager: HyperZoneDatabaseManager
    ) {
        val limboProvider = owner as? HyperZoneLimboProvider
            ?: throw IllegalStateException("OfflineSubModule requires HyperZoneLimboProvider owner")
        val playerAccessorProvider = owner as? HyperZonePlayerAccessorProvider
            ?: throw IllegalStateException("OfflineSubModule requires HyperZonePlayerAccessorProvider owner")

        OfflineAuthCommandRegistrar.registerAll(
            proxy = proxy,
            authServer = limboProvider.limboAuth.authServer,
            playerAccessor = playerAccessorProvider.hyperZonePlayers
        )
        proxy.eventManager.register(owner, OfflineLimboEventListener())
        Logger.getLogger("HyperZoneLogin-AuthOffline")
            .info("OfflineSubModule 已加载，离线 Limbo 命令与提示监听器已注册")
    }
}