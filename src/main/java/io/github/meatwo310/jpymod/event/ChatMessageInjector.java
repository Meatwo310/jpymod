package io.github.meatwo310.jpymod.event;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.config.ServerConfig;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = JPYMod.MODID, bus = Bus.FORGE)
public class ChatMessageInjector {
    private static final Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onServerChat(ServerChatEvent event) {
        try {
            ServerPlayer player = event.getPlayer();
            String playerName = player.getName().getString();

            JsonObject suffixPlayersObject = ServerConfig.getSuffixPlayers();
            if (!suffixPlayersObject.has(playerName)) {
                return;
            }

            MutableComponent message = event.getMessage().copy();
            message.append(suffixPlayersObject.get(playerName).getAsString());
            event.setMessage(message);
        } catch (Exception e) {
            LOGGER.error("Error processing chat message for player: {}", event.getPlayer().getName().getString(), e);
        }
    }
}
