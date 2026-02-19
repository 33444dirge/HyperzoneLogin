package fun.iiii.h2l.api.module;

import com.velocitypowered.api.proxy.ProxyServer;
import fun.iiii.h2l.api.db.HyperZoneDatabaseManager;

import java.nio.file.Path;

public interface HyperSubModule {
    void register(Object owner, ProxyServer proxy, Path dataDirectory, HyperZoneDatabaseManager databaseManager);
}
