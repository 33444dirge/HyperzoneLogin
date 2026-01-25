package icu.h2l.login

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import icu.h2l.login.command.HyperZoneLoginCommand
import icu.h2l.login.config.HyperZoneLoginConfig
import icu.h2l.login.limbo.LimboAuth
import icu.h2l.login.listener.EventListener
import icu.h2l.login.manager.LoginServerManager
import java.nio.file.Files
import java.nio.file.Path
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.dataClassFieldDiscoverer
import org.spongepowered.configurate.objectmapping.ObjectMapper

@Suppress("ANNOTATION_WILL_BE_APPLIED_ALSO_TO_PROPERTY_OR_FIELD")
class HyperZoneLoginMain @Inject constructor(
    private val server: ProxyServer,
    val logger: ComponentLogger,
    @DataDirectory private val dataDirectory: Path,
    private val injector: Injector
) {
    lateinit var loginServerManager: LoginServerManager
    lateinit var limboServerManager: LimboAuth

    companion object {
        private lateinit var instance: HyperZoneLoginMain
        private lateinit var hyperZoneLoginConfig: HyperZoneLoginConfig

        @JvmStatic
        fun getInstance(): HyperZoneLoginMain = instance

        @JvmStatic
        fun getConfig(): HyperZoneLoginConfig = hyperZoneLoginConfig
    }

    init {
        instance = this
    }

    @Subscribe
    fun onEnable(event: ProxyInitializeEvent) {
        loadConfig()

        loginServerManager = LoginServerManager()
        limboServerManager = LimboAuth(server)
        limboServerManager.load()

        proxy.commandManager.register("hzl", HyperZoneLoginCommand())
        proxy.eventManager.register(this, EventListener())
        proxy.eventManager.register(this, limboServerManager)

    }

    val proxy: ProxyServer
        get() = server

    private fun loadConfig() {
        val path = dataDirectory.resolve("config.conf")
        val firstCreation = Files.notExists(path)
        val loader = HoconConfigurationLoader.builder()
            .defaultOptions { opts: ConfigurationOptions ->
                opts
                    .shouldCopyDefaults(true)
                    .header(
                        """
                            HyperZoneLogin | by ksqeib
                            
                        """.trimIndent()
                    ).serializers { s ->
                        s.registerAnnotatedObjects(
                            ObjectMapper.factoryBuilder().addDiscoverer(dataClassFieldDiscoverer()).build()
                        )
                    }
            }
            .path(path)
            .build()
        val node = loader.load()
        val config = node.get(HyperZoneLoginConfig::class.java)
        if (firstCreation) {
            node.set(config)
            loader.save(node)
        }
        if (config != null) {
            hyperZoneLoginConfig = config
        }
    }
}

