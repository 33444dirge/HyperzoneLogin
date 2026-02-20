package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import icu.h2l.api.player.HyperZonePlayerAccessor
import net.kyori.adventure.text.Component

class LoginCommand(
	private val playerAccessor: HyperZonePlayerAccessor
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

		if (args[0] != "123456") {
			source.sendPlainMessage("§c密码错误")
			return
		}

		val hyperZonePlayer = playerAccessor.getOrCreate(source)
		hyperZonePlayer.overVerify()
		hyperZonePlayer.sendMessage(Component.text("§a登录成功，已通过验证"))
	}
}
