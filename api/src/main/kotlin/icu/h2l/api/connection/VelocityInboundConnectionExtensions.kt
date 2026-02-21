package icu.h2l.api.connection

import com.velocitypowered.api.proxy.InboundConnection
import com.velocitypowered.proxy.connection.MinecraftConnection
import com.velocitypowered.proxy.connection.client.InitialInboundConnection
import com.velocitypowered.proxy.connection.client.LoginInboundConnection

private val initialInboundConnectionDelegateField by lazy {
    InitialInboundConnection::class.java.getDeclaredField("delegate").apply {
        isAccessible = true
    }
}

private val delegateGetConnectionMethod by lazy {
    initialInboundConnectionDelegateField.type.getDeclaredMethod("getConnection").apply {
        isAccessible = true
    }
}

fun InboundConnection.getLoginChannel() = (this as LoginInboundConnection).let { loginInboundConnection ->
    val delegate = initialInboundConnectionDelegateField.get(loginInboundConnection)
    val minecraftConnection = delegateGetConnectionMethod.invoke(delegate) as MinecraftConnection
    minecraftConnection.channel
}

fun InboundConnection.getChannel() = (this as MinecraftConnection).channel

fun InboundConnection.getInitialChannel() = (this as InitialInboundConnection).connection.channel