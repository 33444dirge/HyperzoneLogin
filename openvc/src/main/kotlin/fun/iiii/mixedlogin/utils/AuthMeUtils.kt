package `fun`.iiii.mixedlogin.utils

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import `fun`.iiii.mixedlogin.enums.SendMode
import org.slf4j.Logger
import java.util.*

object AuthMeUtils {
    fun getFirstArgument(command: String): String {
        return command.split(" ").getOrNull(0) ?: ""
    }

    val RANDOM: Random = Random()

    fun serverToSend(
        sendMode: SendMode, proxy: ProxyServer, servers: List<String?>, attempts: Int, logger: Logger
    ): Pair<String?, RegisteredServer?> {
        when (sendMode) {
            SendMode.TO_FIRST -> {
                var sv: Optional<RegisteredServer?>
                for (st in servers) {
                    sv = proxy.getServer(st)
                    if (sv.isPresent) {
                        return Pair(st, sv.get())
                    } else {
                        logger.error("无效服务器: ${st}, sendMode: $sendMode")
                    }
                }
                logger.error("找不到有效服务器 sendMode: $sendMode 服务器列表: $servers")
                return Pair(null, null)
            }

            SendMode.TO_EMPTIEST_SERVER -> {
                var emptiest: RegisteredServer? = null
                var optional = Optional.empty<RegisteredServer>()
                for (st in servers) {
                    optional = proxy.getServer(st)
                    if (optional.isPresent) {
                        val actualsv = optional.get()
                        val actualConnected = actualsv.playersConnected.size
                        if (actualConnected == 0) {
                            return Pair(st, actualsv)
                        }
                        if (emptiest == null || actualConnected < emptiest.playersConnected.size) {
                            emptiest = actualsv
                        }
                    }
                }
                return Pair(optional.map { sv: RegisteredServer -> sv.serverInfo.name }.orElse(null), emptiest)
            }

            SendMode.RANDOM -> {
                var server: Optional<RegisteredServer?>
                if (servers.size == 1) {
                    server = proxy.getServer(servers[0])
                    if (server.isPresent) {
                        return Pair(server.get().serverInfo.name, server.get())
                    } else {
                        logger.error("无效服务器: ${server}, sendMode: $sendMode")
                    }
                }
                for (i in 0..<attempts) {
                    val value: Int = RANDOM.nextInt(servers.size)
                    val st = servers[value]
                    server = proxy.getServer(st)
                    if (server.isPresent) {
                        return Pair(server.get().serverInfo.name, server.get())
                    } else {
                        logger.error("无效服务器: ${st}, sendMode: $sendMode")
                    }
                }
                logger.error("找不到有效服务器 sendMode: $sendMode 服务器列表: $servers")
                return Pair(null, null)
            }
        }
    }
} 