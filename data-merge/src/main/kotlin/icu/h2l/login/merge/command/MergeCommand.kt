package icu.h2l.login.merge.command

import com.velocitypowered.api.command.SimpleCommand
import java.util.concurrent.atomic.AtomicBoolean

class MergeCommand(
    private val runMlMigration: () -> String
) : SimpleCommand {
    private val running = AtomicBoolean(false)

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) {
            sender.sendPlainMessage("§e/hzl-merge ml")
            return
        }

        if (!args[0].equals("ml", ignoreCase = true)) {
            sender.sendPlainMessage("§c未知子命令: ${args[0]}")
            sender.sendPlainMessage("§e可用子命令: ml")
            return
        }

        if (!running.compareAndSet(false, true)) {
            sender.sendPlainMessage("§c迁移正在执行中，请稍后再试")
            return
        }

        try {
            sender.sendPlainMessage("§e开始执行 ML 迁移，请稍候...")
            val summary = runMlMigration()
            sender.sendPlainMessage("§a迁移完成: $summary")
            sender.sendPlainMessage("§a详细日志已输出到 merge-ml.log")
        } catch (ex: Exception) {
            sender.sendPlainMessage("§c迁移失败: ${ex.message}")
        } finally {
            running.set(false)
        }
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("hyperzonelogin.admin")
    }
}
