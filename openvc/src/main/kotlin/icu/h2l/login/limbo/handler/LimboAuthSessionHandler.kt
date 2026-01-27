package icu.h2l.login.limbo.handler

import com.velocitypowered.api.proxy.Player
import net.elytrium.limboapi.api.Limbo
import net.elytrium.limboapi.api.LimboSessionHandler
import net.elytrium.limboapi.api.player.LimboPlayer
import java.util.concurrent.atomic.AtomicBoolean

class LimboAuthSessionHandler(private val proxyPlayer: Player) : LimboSessionHandler {
    private lateinit var player: LimboPlayer
    
    /**
     * 标记是否已经完成over验证
     */
    private val isOverVerified = AtomicBoolean(false)

    override fun onSpawn(server: Limbo, player: LimboPlayer) {
        this.player = player
        this.player.disableFalling()
    }

    override fun onChat(message: String) {
        proxyPlayer.sendPlainMessage("SUCCESS MES")

        if (message.contains("login")) {
            player.disconnect()
        }
    }
    
    /**
     * 完成验证，结束Limbo状态
     * 此方法由AuthManager在Yggdrasil验证成功时调用
     */
    fun overVerify() {
        if (isOverVerified.compareAndSet(false, true)) {
            // 只执行一次
            if (::player.isInitialized) {
                player.disconnect()
            }
        }
    }
    
    /**
     * 检查是否已经完成over验证
     */
    fun isOverVerified(): Boolean {
        return isOverVerified.get()
    }
}
