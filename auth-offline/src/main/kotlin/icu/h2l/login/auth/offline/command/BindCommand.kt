package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import icu.h2l.login.auth.offline.service.OfflineAuthService

class BindCommand(
    private val authService: OfflineAuthService
) : BasePlaceholderAuthCommand("/bind <password> <password>") {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        if (source !is Player) {
            source.sendPlainMessage("§c该命令只能由玩家执行")
            return
        }

        val args = invocation.arguments()
        if (args.size != 2) {
            source.sendPlainMessage("§e/bind <password> <password>")
            return
        }

        if (args[0] != args[1]) {
            source.sendPlainMessage("§c两次密码不一致")
            return
        }

        val result = authService.bind(source, args[0])
        source.sendPlainMessage(result.message)
    }
}