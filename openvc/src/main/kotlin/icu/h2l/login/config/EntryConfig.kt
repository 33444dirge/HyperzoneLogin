package icu.h2l.login.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class EntryConfig {

    @Comment("此验证服务的 ID，用于识别验证服务的唯一标识")
    var id: String = "Example"

    @Comment("验证服务的别称，用于一些指令结果或其他用途的内容显示")
    var name: String = "Unnamed"

    @Comment(
        """验证服务类型，支持以下值:
        Mojang - 官方 Yggdrasil Java 版账号验证服务
        Offline - 离线服务
        BLESSING_SKIN - Blessing Skin 的伪正版验证服务
        CUSTOM_YGGDRASIL - 自定义 Yggdrasil 伪正版验证服务
        FLOODGATE - Geyser 的 Floodgate（Xbox账号）验证服务"""
    )
    var serviceType: String = "Mojang"

    @Comment("Yggdrasil 类型账号验证服务配置")
    var yggdrasilAuth: YggdrasilAuth = YggdrasilAuth()

    @ConfigSerializable
    class YggdrasilAuth {
        @Comment("Yggdrasil 在 hasJoined 阶段时请求是否追加用户 IP 信息")
        var trackIp: Boolean = false

        @Comment("设置 Yggdrasil hasJoined 验证超时时间")
        var timeout: Int = 10000

        @Comment("设置 Yggdrasil hasJoined 网络错误时的重试次数")
        var retry: Int = 0

        @Comment("设置 Yggdrasil hasJoined 重试请求延迟")
        var retryDelay: Int = 0

        @Comment("OFFICIAL 专用设置")
        var official: Official = Official()

        @Comment("BLESSING_SKIN 专用设置")
        var blessingSkin: BlessingSkin = BlessingSkin()

        @Comment("CUSTOM_YGGDRASIL 专用设置")
        var custom: Custom = Custom()

        @Comment("设置 Yggdrasil hasJoined 代理")
        var authProxy: Proxy = Proxy()

        @ConfigSerializable
        class Official {
            @Comment("可选，指定自定义 Session 验证服务器地址")
            var sessionServer: String = "https://sessionserver.mojang.com"
        }

        @ConfigSerializable
        class BlessingSkin {
            @Comment("指定 Blessing Skin Yggdrasil API 地址")
            var apiRoot: String = "https://example.com/api/yggdrasil"
        }

        @ConfigSerializable
        class Custom {
            @Comment(
                """设置 Yggdrasil hasJoined 请求验证链接设置
                占位变量: {0}/{username}, {1}/{serverId}, {2}/{ip}"""
            )
            var url: String = "https://example.com/session/minecraft/hasJoined?username={0}&serverId={1}{2}"

            @Comment(
                """设置 Yggdrasil hasJoined 请求验证方式
                GET - 此方式被绝大多数验证服务器（包括官方）采用
                POST - 此方式被极少数验证服务器采用"""
            )
            var method: String = "GET"

            @Comment("设置 Yggdrasil hasJoined 的 url 和 postContent 节点 {ip} 变量内容")
            var trackIpContent: String = "&ip={0}"

            @Comment("设置 Yggdrasil hasJoined 发送 POST 验证请求的内容")
            var postContent: String = """{"username":"{0}", "serverId":"{1}"}"""
        }
    }

    @ConfigSerializable
    class Proxy {
        @Comment(
            """设置代理类型
            DIRECT - 直接连接、或没有代理
            HTTP - 表示高级协议(如HTTP或FTP)的代理
            SOCKS - 表示一个SOCKS (V4或V5)代理"""
        )
        var type: String = "DIRECT"

        @Comment("代理服务器地址")
        var hostname: String = "127.0.0.1"

        @Comment("代理服务器端口")
        var port: Int = 1080

        @Comment("代理鉴权用户名，留空则不进行鉴权")
        var username: String = ""

        @Comment("代理鉴权密码")
        var password: String = ""
    }
}
