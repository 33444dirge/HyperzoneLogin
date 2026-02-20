package icu.h2l.api.limbo

import net.elytrium.limboapi.api.Limbo

interface HyperZoneLimbo {
    val authServer: Limbo
}

interface HyperZoneLimboProvider {
    val limboAuth: HyperZoneLimbo
}
