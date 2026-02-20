package icu.h2l.api.player

import com.velocitypowered.api.proxy.Player
import java.util.UUID

interface HyperZonePlayerAccessor {
    fun getOrCreate(proxyPlayer: Player): HyperZonePlayer
    fun getByUuid(uuid: UUID): HyperZonePlayer?
    fun getByName(name: String): HyperZonePlayer?
    fun getByNameOrUuid(name: String, uuid: UUID): HyperZonePlayer?
}

interface HyperZonePlayerAccessorProvider {
    val hyperZonePlayers: HyperZonePlayerAccessor
}
