package io.github.meatwo310.jpymod.mixin.client;

import io.github.meatwo310.jpymod.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.management.module.ItemSharingModule;

@Mixin(
        value = ItemSharingModule.Client.class
        // remap = false すると死ぬ(1敗)
)
public class ItemSharingModuleClientMixin {
    @Inject(method = "render(" +
            "Lnet/minecraft/client/Minecraft;" +
            "Lnet/minecraft/client/gui/GuiGraphics;" +
            "Ljava/lang/String;" +
            "FFFLnet/minecraft/network/chat/Style;" +
            "I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void render(Minecraft mc, GuiGraphics guiGraphics, String before, float extraShift, float x, float y,
                               Style style, int color, CallbackInfo ci) {
        float xMove = ClientConfig.SHIFT_SHARED_ITEM_X.get().floatValue();
        float yMove = ClientConfig.SHIFT_SHARED_ITEM_Y.get().floatValue();
        float scale = ClientConfig.SCALE_SHARED_ITEM.get().floatValue();

        guiGraphics.pose().translate(xMove, yMove, 0.0f);
        guiGraphics.pose().scale(scale, scale, scale);
    }
}
