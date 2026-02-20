package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import icu.h2l.login.auth.offline.service.OfflineAuthService

class LoginCommand(
	private val authService: OfflineAuthService
	) : BasePlaceholderAuthCommand("/login <password>") {
	override fun execute(invocation: SimpleCommand.Invocation) {
		val source = invocation.source()
		if (source !is Player) {
			source.sendPlainMessage("§c该命令只能由玩家执行")
			return
		}

		val args = invocation.arguments()
		if (args.size != 1) {
			source.sendPlainMessage("§e/login <password>")
			return
		}

		val result = authService.login(source, args[0])
		source.sendPlainMessage(result.message)
	}
}
