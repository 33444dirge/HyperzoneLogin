package icu.h2l.login.util

import com.google.common.collect.ImmutableList
import com.velocitypowered.api.util.GameProfile
import icu.h2l.login.listener.EventListener.Companion.EXPECTED_NAME_PREFIX
import icu.h2l.login.listener.EventListener.Companion.REMAP_PREFIX
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.random.Random

object RemapUtils {
    fun genProfile(username: String, prefix: String): GameProfile {
        return GameProfile(
            genUUID(username, prefix), username,
            ImmutableList.of()
        )
    }

    fun genUUID(username: String, prefix: String): UUID {
        return UUID.nameUUIDFromBytes(("$prefix:$username").toByteArray(StandardCharsets.UTF_8))
    }

    fun randomProfile(): GameProfile {
        val randomId = String.format("%06d", Random.nextInt(1_000_000))
        val newName = "$EXPECTED_NAME_PREFIX$randomId"
        return genProfile(newName, REMAP_PREFIX)
    }
}