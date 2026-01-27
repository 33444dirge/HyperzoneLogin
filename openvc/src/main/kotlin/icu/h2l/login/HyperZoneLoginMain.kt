package icu.h2l.login

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import icu.h2l.login.command.HyperZoneLoginCommand
import icu.h2l.login.config.OfflineMatchConfig
import icu.h2l.login.limbo.LimboAuth
import icu.h2l.login.manager.EntryConfigManager
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
    lateinit var entryConfigManager: EntryConfigManager

    companion object {
        private lateinit var instance: HyperZoneLoginMain
        private lateinit var offlineMatchConfig: OfflineMatchConfig

        @JvmStatic
        fun getInstance(): HyperZoneLoginMain = instance

        @JvmStatic
        fun getConfig(): OfflineMatchConfig = offlineMatchConfig
    }

    init {
        instance = this
    }

    @Subscribe
    fun onEnable(event: ProxyInitializeEvent) {
        loadConfig()
        loadEntryConfigs()

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
        val path = dataDirectory.resolve("offlinematch.conf")
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
        val config = node.get(OfflineMatchConfig::class.java)
        if (firstCreation) {
            node.set(config)
            loader.save(node)
        }
        if (config != null) {
            offlineMatchConfig = config
        }
    }

    private fun loadEntryConfigs() {
        entryConfigManager = EntryConfigManager(dataDirectory, logger)
        entryConfigManager.loadAllConfigs()
    }
}

