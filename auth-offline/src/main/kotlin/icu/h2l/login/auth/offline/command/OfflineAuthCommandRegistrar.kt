package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.proxy.ProxyServer
import icu.h2l.api.player.HyperZonePlayerAccessor
import net.elytrium.limboapi.api.Limbo

object OfflineAuthCommandRegistrar {
    fun registerAll(proxy: ProxyServer, authServer: Limbo, playerAccessor: HyperZonePlayerAccessor) {
        authServer.registerCommand(
            proxy.commandManager.metaBuilder("login").aliases("l").build(),
            LoginCommand(playerAccessor)
        )
        authServer.registerCommand(
            proxy.commandManager.metaBuilder("register").aliases("reg").build(),
            RegisterCommand()
        )
        authServer.registerCommand(
            proxy.commandManager.metaBuilder("changepassword").aliases("cpass", "changepass").build(),
            ChangePasswordCommand()
        )
        authServer.registerCommand(
            proxy.commandManager.metaBuilder("logout").build(),
            LogoutCommand()
        )
        authServer.registerCommand(
            proxy.commandManager.metaBuilder("unregister").aliases("delaccount").build(),
            UnregisterCommand()
        )
    }
}
