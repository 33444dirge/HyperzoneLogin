package icu.h2l.api.limbo.handler

import net.kyori.adventure.text.Component

interface LimboAuthSessionOverVerify {
    fun overVerify()

    fun isOverVerified(): Boolean

    fun sendMessage(message: Component)
}