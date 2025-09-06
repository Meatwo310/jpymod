package io.github.meatwo310.jpymod.compat.lightmanscurrency;

import com.mojang.logging.LogUtils;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.compat.lightmanscurrency.rules.types.ForexLink;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = JPYMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LCCompat {
    @SubscribeEvent
    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("lightmanscurrency")) {
            try {
                TraderAPI.API.RegisterTradeRule(ForexLink.TYPE);
            } catch (Exception e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        }
    }
}
