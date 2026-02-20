package icu.h2l.login.auth.offline.command

import icu.h2l.api.command.HyperChatCommandManager
import icu.h2l.api.command.HyperChatCommandRegistration
import icu.h2l.login.auth.offline.service.OfflineAuthService

object OfflineAuthCommandRegistrar {
    fun registerAll(
        commandManager: HyperChatCommandManager,
        authService: OfflineAuthService
    ) {
        commandManager.register(
            HyperChatCommandRegistration(
                name = "login",
                aliases = setOf("l"),
                command = LoginCommand(authService)
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "register",
                aliases = setOf("reg"),
                command = RegisterCommand(authService)
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "bind",
                aliases = setOf("b"),
                command = BindCommand(authService)
            )
        )
        commandManager.register(
            HyperChatCommandRegistration(
                name = "changepassword",
                aliases = setOf("cpass", "changepass"),
                command = ChangePasswordCommand(authService)
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
                command = UnregisterCommand(authService)
            )
        )
    }
}
