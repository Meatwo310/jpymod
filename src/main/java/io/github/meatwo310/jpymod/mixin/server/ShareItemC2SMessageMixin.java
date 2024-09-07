package io.github.meatwo310.jpymod.mixin.server;

import com.mojang.logging.LogUtils;
import io.github.meatwo310.jpymod.compat.SDLinkCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.base.network.message.ShareItemC2SMessage;

@Mixin(
        value = ShareItemC2SMessage.class,
        remap = false
)
public class ShareItemC2SMessageMixin {
    @Unique private static final Logger jpymod$LOGGER = LogUtils.getLogger();

    @Shadow public ItemStack toShare;

    @Inject(
            method = "lambda$receive$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/violetmoon/zeta/network/ZetaNetworkHandler;" +
                            "sendToAllPlayers(" +
                            "Lorg/violetmoon/zeta/network/IZetaMessage;" +
                            "Lnet/minecraft/server/MinecraftServer;" +
                            ")V"
            )
    )
    private void onReceive(ServerPlayer sender, CallbackInfo ci) {
        try {
            if (!SDLinkCompat.isSDLinkLoaded()) return;
            SDLinkCompat.sendToDiscord(sender, toShare);
        } catch (Exception e) {
            jpymod$LOGGER.error("Failed to send item share message to Discord", e);
        }
    }
}
