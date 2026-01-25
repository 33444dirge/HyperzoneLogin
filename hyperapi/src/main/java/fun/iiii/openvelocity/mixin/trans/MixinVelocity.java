package fun.iiii.openvelocity.mixin.trans;

import com.velocitypowered.proxy.VelocityServer;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VelocityServer.class)
public class MixinVelocity {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruction(CallbackInfo ci) {
        Logger testLogger=LogManager.getLogger(VelocityServer.class);
        testLogger.info("[HyperVC]如有问题请提交issue或加QQ群反馈 群号：946864759");
        testLogger.info("[HyperVC]此为闭源项目，未授权任何形式二次分发，如果您在非官方群下载到此插件，请反馈给官方");
        testLogger.info("[HyperVC]如有违规使用，官方将采取必要的法律手段");
        testLogger.info("[HyperVC]made by 庆灵(QQ:2388990095)");
    }
}
