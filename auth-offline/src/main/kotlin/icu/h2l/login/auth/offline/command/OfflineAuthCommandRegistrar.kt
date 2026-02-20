package icu.h2l.login.auth.offline.command

import icu.h2l.api.command.HyperChatCommandManager
import icu.h2l.api.command.HyperChatCommandRegistration
import icu.h2l.api.player.HyperZonePlayerAccessor

object OfflineAuthCommandRegistrar {
    fun registerAll(
        commandManager: HyperChatCommandManager,
        playerAccessor: HyperZonePlayerAccessor
    ) {
        commandManager.register(
            HyperChatCommandRegistration(
                name = "login",
                aliases = setOf("l"),
                command = LoginCommand(playerAccessor)
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "register",
                aliases = setOf("reg"),
                command = RegisterCommand()
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "changepassword",
                aliases = setOf("cpass", "changepass"),
                command = ChangePasswordCommand()
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "logout",
                command = LogoutCommand()
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "unregister",
                aliases = setOf("delaccount"),
                command = UnregisterCommand()
            )
        )
    }
}
