package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import icu.h2l.login.auth.offline.service.OfflineAuthService

class UnregisterCommand(
	private val authService: OfflineAuthService
) : BasePlaceholderAuthCommand("/unregister <password>") {
	override fun execute(invocation: SimpleCommand.Invocation) {
		val source = invocation.source()
		if (source !is Player) {
			source.sendPlainMessage("§c该命令只能由玩家执行")
			return
		}

		val args = invocation.arguments()
		if (args.size != 1) {
			source.sendPlainMessage("§e/unregister <password>")
			return
		}

		val result = authService.unregister(source, args[0])
		source.sendPlainMessage(result.message)
	}
}
