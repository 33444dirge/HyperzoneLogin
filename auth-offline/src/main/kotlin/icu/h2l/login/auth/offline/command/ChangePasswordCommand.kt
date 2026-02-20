package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import icu.h2l.login.auth.offline.service.OfflineAuthService

class ChangePasswordCommand(
	private val authService: OfflineAuthService
) : BasePlaceholderAuthCommand("/changepassword <oldPassword> <newPassword>") {
	override fun execute(invocation: SimpleCommand.Invocation) {
		val source = invocation.source()
		if (source !is Player) {
			source.sendPlainMessage("§c该命令只能由玩家执行")
			return
		}

		val args = invocation.arguments()
		if (args.size != 2) {
			source.sendPlainMessage("§e/changepassword <oldPassword> <newPassword>")
			return
		}

		val result = authService.changePassword(source, args[0], args[1])
		source.sendPlainMessage(result.message)
	}
}
