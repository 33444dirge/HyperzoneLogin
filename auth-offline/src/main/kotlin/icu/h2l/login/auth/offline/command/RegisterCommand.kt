package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import icu.h2l.login.auth.offline.service.OfflineAuthService

class RegisterCommand(
	private val authService: OfflineAuthService
) : BasePlaceholderAuthCommand("/register <password> <password>") {
	override fun execute(invocation: SimpleCommand.Invocation) {
		val source = invocation.source()
		if (source !is Player) {
			source.sendPlainMessage("§c该命令只能由玩家执行")
			return
		}

		val args = invocation.arguments()
		if (args.size != 2) {
			source.sendPlainMessage("§e/register <password> <password>")
			return
		}

		if (args[0] != args[1]) {
			source.sendPlainMessage("§c两次密码不一致")
			return
		}

		val result = authService.register(source, args[0])
		source.sendPlainMessage(result.message)
	}
}
