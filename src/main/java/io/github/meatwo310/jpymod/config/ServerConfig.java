package io.github.meatwo310.jpymod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue FOREX_EXCHANGE_RATE = BUILDER
            .comment("The exchange rate from USD to JPY for the Forex Link trade rule.")
            .defineInRange("forexExchangeRate", 140, 1, 1000);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
