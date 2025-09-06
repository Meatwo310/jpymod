package io.github.meatwo310.jpymod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue FOREX_EXCHANGE_RATE = BUILDER
            .comment("The exchange rate from USD to JPY for the Forex Link trade rule.")
            .comment("Set API key in common config to enable automatic updates.")
            .defineInRange("forexExchangeRate", 140, 1, 1000);

    public static final ForgeConfigSpec.BooleanValue FOREX_REFRESH_ON_STARTUP = BUILDER
            .comment("If true, the Forex exchange rate will be fetched from Alpha Vantage when the server starts.")
            .define("forexRefreshOnStartup", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
