package io.github.meatwo310.jpymod.event;

import io.github.meatwo310.jpymod.command.JPYCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandRegisterer {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        JPYCommand.register(event.getDispatcher());
    }
}
