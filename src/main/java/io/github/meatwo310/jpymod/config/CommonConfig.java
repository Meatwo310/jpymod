package io.github.meatwo310.jpymod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<? extends String> ALPHA_VANTAGE_API_KEY = BUILDER
            .comment("Your Alpha Vantage API key for fetching real-time Forex data.")
            .comment("Refreshed every two hours if the key is provided.")
            .comment("Must be enabled in the *server* config.")
            .define("alphaVantageApiKey", "");

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
