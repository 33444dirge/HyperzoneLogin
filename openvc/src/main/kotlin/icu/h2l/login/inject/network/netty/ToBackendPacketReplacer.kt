package icu.h2l.login.inject.network.netty

import com.velocitypowered.api.network.HandshakeIntent
import com.velocitypowered.api.network.ProtocolVersion
import com.velocitypowered.api.util.GameProfile
import com.velocitypowered.proxy.config.PlayerInfoForwarding
import com.velocitypowered.proxy.config.VelocityConfiguration
import com.velocitypowered.proxy.connection.ConnectionTypes
import com.velocitypowered.proxy.connection.MinecraftConnection
import com.velocitypowered.proxy.connection.PlayerDataForwarding
import com.velocitypowered.proxy.connection.PlayerDataForwarding.createBungeeGuardForwardingAddress
import com.velocitypowered.proxy.connection.PlayerDataForwarding.createLegacyForwardingAddress
import com.velocitypowered.proxy.connection.backend.VelocityServerConnection
import com.velocitypowered.proxy.connection.client.ConnectedPlayer
import com.velocitypowered.proxy.connection.forge.legacy.LegacyForgeConstants
import com.velocitypowered.proxy.connection.forge.modern.ModernForgeConnectionType
import com.velocitypowered.proxy.protocol.ProtocolUtils
import com.velocitypowered.proxy.protocol.packet.HandshakePacket
import com.velocitypowered.proxy.protocol.packet.LoginPluginResponsePacket
import com.velocitypowered.proxy.protocol.packet.ServerLoginPacket
import icu.h2l.api.player.HyperZonePlayer
import icu.h2l.login.HyperZoneLoginMain
import icu.h2l.login.manager.HyperZonePlayerManager
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import java.net.InetSocketAddress
import java.util.function.Supplier

class ToBackendPacketReplacer : ChannelOutboundHandlerAdapter() {
    private lateinit var mcConnection: MinecraftConnection
    private lateinit var velocityServerConnection: VelocityServerConnection
    private lateinit var player: ConnectedPlayer
    private lateinit var hyperPlayer: HyperZonePlayer

    private lateinit var config: VelocityConfiguration

    private fun replaceMessage(
        channel: Channel,
        ctx: ChannelHandlerContext,
        msg: Any?
    ) = when (msg) {
//        BUNGEEGUARD LEGACY
        is HandshakePacket -> {
            genHandshake(ctx, msg)
        }
//        MC自己的
        is ServerLoginPacket -> {
            genServerLogin(ctx, msg)
        }
//        MODERN
        is LoginPluginResponsePacket -> {
//  com.velocitypowered.proxy.connection.backend.LoginSessionHandler.handle(com.velocitypowered.proxy.protocol.packet.LoginPluginMessagePacket)
            genLoginPluginResponse(ctx, msg)
        }

        else -> msg
    }

    private fun genLoginPluginResponse(
        ctx: ChannelHandlerContext,
        msg: LoginPluginResponsePacket
    ): LoginPluginResponsePacket {
        if (config.getPlayerInfoForwardingMode() == PlayerInfoForwarding.MODERN) {
            val buf = msg.content()
            var requestedForwardingVersion = PlayerDataForwarding.MODERN_DEFAULT
            // Check version
            if (buf.readableBytes() >= 1) {
                requestedForwardingVersion = ProtocolUtils.readVarInt(buf)
            }
            val forwardingData = PlayerDataForwarding.createForwardingData(
                config.getForwardingSecret(),
                getPlayerRemoteAddressAsString(),
                player.getProtocolVersion(),
                getGameProfile(),
                player.getIdentifiedKey(),
                requestedForwardingVersion
            )

            val response = LoginPluginResponsePacket(
                msg.getId(), true, forwardingData
            )
            return response
        }

        return msg

    }

    private lateinit var fillAddr: InetSocketAddress

    private fun genHandshake(ctx: ChannelHandlerContext, msg: HandshakePacket): HandshakePacket {
        val forwardingMode: PlayerInfoForwarding? = config.getPlayerInfoForwardingMode()
//        val player = HyperZonePlayerManager.getByChannel(ctx.channel()).proxyPlayer!! as ConnectedPlayer


        // Initiate the handshake.
        val protocolVersion: ProtocolVersion? = player.getConnection().getProtocolVersion()
        val playerVhost: String? = player.getVirtualHost()
            .orElseGet { fillAddr }
            .getHostString()

        val handshake = HandshakePacket()
        handshake.setIntent(HandshakeIntent.LOGIN)
        handshake.setProtocolVersion(protocolVersion)
        if (forwardingMode == PlayerInfoForwarding.LEGACY) {
            handshake.setServerAddress(createLegacyForwardingAddress())
        } else if (forwardingMode == PlayerInfoForwarding.BUNGEEGUARD) {
            val secret: ByteArray = config.getForwardingSecret()
            handshake.setServerAddress(createBungeeGuardForwardingAddress(secret))
        } else if (player.getConnection().getType() === ConnectionTypes.LEGACY_FORGE) {
            handshake.setServerAddress(playerVhost + LegacyForgeConstants.HANDSHAKE_HOSTNAME_TOKEN)
        } else if (player.getConnection().getType() is ModernForgeConnectionType) {
            handshake.setServerAddress(
                playerVhost + (player
                    .getConnection().getType() as ModernForgeConnectionType).getModernToken()
            )
        } else {
            handshake.setServerAddress(playerVhost)
        }

        handshake.setPort(
            player.getVirtualHost()
                .orElseGet(Supplier { fillAddr })
                .getPort()
        )

        return handshake
    }

    private fun createLegacyForwardingAddress(): String {
        return createLegacyForwardingAddress(
            player.getVirtualHost().orElseGet(Supplier { fillAddr })
                .getHostString(),
            getPlayerRemoteAddressAsString(),
            getGameProfile()
        )
    }

    private fun createBungeeGuardForwardingAddress(forwardingSecret: ByteArray): String {
        return createBungeeGuardForwardingAddress(
            player.getVirtualHost().orElseGet(Supplier { fillAddr })
                .getHostString(),
            getPlayerRemoteAddressAsString(),
            getGameProfile(),
            forwardingSecret
        )
    }

    fun getGameProfile(): GameProfile {
        return hyperPlayer.getGameProfile()
//        return RemapUtils.randomProfile()
    }

    fun getPlayerRemoteAddressAsString(): String {
        val addr: String = player.getRemoteAddress().getAddress().getHostAddress()
        val ipv6ScopeIdx = addr.indexOf('%')
        if (ipv6ScopeIdx == -1) {
            return addr
        } else {
            return addr.substring(0, ipv6ScopeIdx)
        }
    }


    private fun genServerLogin(ctx: ChannelHandlerContext, msg: ServerLoginPacket): ServerLoginPacket {
        if (player.getIdentifiedKey() == null
            && player.getProtocolVersion().noLessThan(ProtocolVersion.MINECRAFT_1_19_3)
        ) {
            return (ServerLoginPacket(hyperPlayer.userName, hyperPlayer.uuid))
        } else {
            return ServerLoginPacket(
                hyperPlayer.userName,
                player.getIdentifiedKey()
            )
        }
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise?) {
        initFields(ctx)

//        println("W: $msg")

        super.write(ctx, replaceMessage(ctx.channel(), ctx, msg), promise)
    }

    private fun initFields(ctx: ChannelHandlerContext) {
        if (::mcConnection.isInitialized) {
            return
        }
        val conn = ctx.channel().pipeline().get(MinecraftConnection::class.java) ?: return

        this.mcConnection = conn
        this.velocityServerConnection = conn.association as VelocityServerConnection
        this.player = velocityServerConnection.player

        this.fillAddr = velocityServerConnection.server.serverInfo.address
        this.hyperPlayer = HyperZonePlayerManager.getByPlayer(player)
        val server = HyperZoneLoginMain.getInstance().proxy
        config = (server.getConfiguration() as VelocityConfiguration)
    }
}