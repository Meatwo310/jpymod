package io.github.meatwo310.jpymod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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
    public static final RegistryObject<Item> YEN_1000 = register("1000yen");
    public static final RegistryObject<Item> YEN_5000 = register("5000yen");
    public static final RegistryObject<Item> YEN_10000 = register("10000yen");

    public static final RegistryObject<Item> USD_1_CENT = register("1_cent");
    public static final RegistryObject<Item> USD_5_CENT = register("5_cent");
    public static final RegistryObject<Item> USD_10_CENT = register("10_cent");
    public static final RegistryObject<Item> USD_25_CENT = register("25_cent");
    public static final RegistryObject<Item> USD_1_DOLLAR = register("1_dollar");
    public static final RegistryObject<Item> USD_5_DOLLAR = register("5_dollar");
    public static final RegistryObject<Item> USD_10_DOLLAR = register("10_dollar");
    public static final RegistryObject<Item> USD_20_DOLLAR = register("20_dollar");
    public static final RegistryObject<Item> USD_50_DOLLAR = register("50_dollar");
    public static final RegistryObject<Item> USD_100_DOLLAR = register("100_dollar");
    
    public static final RegistryObject<Item> CASINO_1_EURO = register("1_casino_euro");
    public static final RegistryObject<Item> CASINO_5_EURO = register("5_casino_euro");
    public static final RegistryObject<Item> CASINO_10_EURO = register("10_casino_euro");
    public static final RegistryObject<Item> CASINO_25_EURO = register("25_casino_euro");
    public static final RegistryObject<Item> CASINO_50_EURO = register("50_casino_euro");
    public static final RegistryObject<Item> CASINO_100_EURO = register("100_casino_euro");
    public static final RegistryObject<Item> CASINO_500_EURO = register("500_casino_euro");
    public static final RegistryObject<Item> CASINO_1000_EURO = register("1000_casino_euro");
    public static final RegistryObject<Item> CASINO_5000_EURO = register("5000_casino_euro");
    public static final RegistryObject<Item> CASINO_10000_EURO = register("10000_casino_euro");

    public static final RegistryObject<CreativeModeTab> JPY_MOD_TAB = CREATIVE_MODE_TABS.register("jpy", () -> CreativeModeTab.builder()
            .title(Component.literal("JPY"))
            .icon(() -> YEN_1000.get().getDefaultInstance())
            .displayItems((parameters, output) -> ITEMS.getEntries().forEach(entry -> output.accept(entry.get().getDefaultInstance()))).build());

    private static RegistryObject<Item> register(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().fireResistant()));
    }

    public JPYMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
