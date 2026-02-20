package icu.h2l.login.manager

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.proxy.Player
import icu.h2l.api.player.HyperZonePlayerAccessor
import icu.h2l.login.player.OpenVcHyperZonePlayer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object HyperZonePlayerManager : HyperZonePlayerAccessor {
    private val playersByUuid = ConcurrentHashMap<UUID, OpenVcHyperZonePlayer>()
    private val playersByName = ConcurrentHashMap<String, OpenVcHyperZonePlayer>()

    override fun getOrCreate(proxyPlayer: Player): OpenVcHyperZonePlayer {
        return playersByUuid.computeIfAbsent(proxyPlayer.uniqueId) {
            OpenVcHyperZonePlayer(proxyPlayer)
        }.also { player ->
            playersByName[proxyPlayer.username.lowercase()] = player
        }
    }

    override fun getByUuid(uuid: UUID): OpenVcHyperZonePlayer? {
        return playersByUuid[uuid]
    }

    override fun getByName(name: String): OpenVcHyperZonePlayer? {
        return playersByName[name.lowercase()]
    }

    override fun getByNameOrUuid(name: String, uuid: UUID): OpenVcHyperZonePlayer? {
        return getByUuid(uuid) ?: getByName(name)
    }

    fun remove(uuid: UUID, name: String? = null) {
        val removed = playersByUuid.remove(uuid)
        if (name != null) {
            playersByName.remove(name.lowercase())
            return
        }

        if (removed != null) {
            playersByName.entries.removeIf { (_, value) -> value === removed }
        }
    }

    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        val player = event.player
        remove(player.uniqueId, player.username)
    }
}
