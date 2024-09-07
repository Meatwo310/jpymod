package io.github.meatwo310.jpymod.compat;

import com.hypherionmc.craterlib.nojang.world.entity.player.BridgedPlayer;
import com.hypherionmc.sdlink.core.managers.HiddenPlayersManager;
import com.hypherionmc.sdlink.platform.SDLinkMCPlatform;
import com.hypherionmc.sdlink.server.ServerEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import shadow.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class SDLinkCompat {
    public static boolean isSDLinkLoaded() {
        return ModList.get().isLoaded("craterlib") && ModList.get().isLoaded("sdlink");
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void sendToDiscord(ServerPlayer sender, ItemStack stack) {
        BridgedPlayer bridgedSender = BridgedPlayer.of(sender);
        if (!SDLinkMCPlatform.INSTANCE.playerIsActive(bridgedSender)) return;
        if (HiddenPlayersManager.INSTANCE.isPlayerHidden(bridgedSender.getStringUUID())) return;

        String serialized = Component.Serializer.toJson(stack.getDisplayName().copy()
                .append(" (`%s`)".formatted(
                        BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()
                ))
        );
        shadow.kyori.adventure.text.Component shadowMessage = GsonComponentSerializer.gson().deserialize(serialized);

        sendToDiscord(bridgedSender, shadowMessage);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void sendToDiscord(BridgedPlayer bridgedSender, shadow.kyori.adventure.text.Component shadowMessage) {
        ServerEvents.getInstance().onServerChatEvent(
                shadowMessage,
                bridgedSender.getDisplayName(),
                SDLinkMCPlatform.INSTANCE.getPlayerSkinUUID(bridgedSender),
                bridgedSender.getGameProfile(),
                false
        );
    }
}
