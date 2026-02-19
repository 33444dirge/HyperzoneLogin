package fun.iiii.h2l.api.event.connection;

import com.velocitypowered.api.event.annotation.AwaitingEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.util.GameProfile;

import java.util.UUID;

/**
 * 进行正版验证类似操作时触发.
 *
 * <p>
 * Velocity typically fires this event asynchronously and does not wait for a response. However,
 * it will wait for all {@link DisconnectEvent}s for every player on the proxy to fire
 * successfully before the proxy shuts down. This event is the sole exception to the
 * {@link AwaitingEvent} contract.
 * </p>
 */

@AwaitingEvent
public final class OnlineAuthEvent {
    private final String userName;
    private final UUID userUUID;
    private final String serverId;
    private final String playerIp;
    private final boolean isOnline;

    private GameProfile gameProfile;

    /**
     * 主要构造方法.
     *
     * @param serverId 服务器ID
     * @param playerIp 玩家IP
     * @param isOnline 是否为正版
     */
    public OnlineAuthEvent(String userName, UUID userUUID, String serverId, String playerIp, boolean isOnline) {
        this.userName = userName;
        this.userUUID = userUUID;
        this.serverId = serverId;
        this.playerIp = playerIp;
        this.isOnline = isOnline;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getPlayerIp() {
        return playerIp;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public String getUserName() {
        return userName;
    }

    public String getServerId() {
        return serverId;
    }


}
