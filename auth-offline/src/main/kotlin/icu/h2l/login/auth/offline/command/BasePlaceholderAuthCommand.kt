package icu.h2l.login.auth.offline.command

import com.velocitypowered.api.command.SimpleCommand

abstract class BasePlaceholderAuthCommand(
    private val usage: String
) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        invocation.source().sendPlainMessage("§e$usage")
        invocation.source().sendPlainMessage("§7该命令功能暂未实现")
    }
}
