package icu.h2l.login.limbo.handler

import com.velocitypowered.api.proxy.Player
import net.elytrium.limboapi.api.Limbo
import net.elytrium.limboapi.api.LimboSessionHandler
import net.elytrium.limboapi.api.player.LimboPlayer

class LimboAuthSessionHandler(private val proxyPlayer: Player) : LimboSessionHandler {
    private lateinit var player: LimboPlayer

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
}
