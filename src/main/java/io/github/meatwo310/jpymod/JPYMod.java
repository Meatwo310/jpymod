package io.github.meatwo310.jpymod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(JPYMod.MODID)
public class JPYMod {
    public static final String MODID = "jpy";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

//    public static final RegistryObject<Item> YEN_1 = ITEMS.register("1yen", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> YEN_5 = ITEMS.register("5yen", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> YEN_10 = ITEMS.register("10yen", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> YEN_50 = ITEMS.register("50yen", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> YEN_100 = ITEMS.register("100yen", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> YEN_500 = ITEMS.register("500yen", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> YEN_1000 = ITEMS.register("1000yen", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> YEN_5000 = ITEMS.register("5000yen", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> YEN_10000 = ITEMS.register("10000yen", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("jpy", () -> CreativeModeTab.builder()
            .title(Component.literal("JPY"))
            .icon(() -> YEN_1000.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
        //                output.accept(YEN_1.get());
        //                output.accept(YEN_5.get());
        //                output.accept(YEN_10.get());
        //                output.accept(YEN_50.get());
        //                output.accept(YEN_100.get());
        //                output.accept(YEN_500.get());
                output.accept(YEN_1000.get());
                output.accept(YEN_5000.get());
                output.accept(YEN_10000.get());
            }).build());

    public JPYMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
