package io.github.meatwo310.jpymod.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import org.slf4j.Logger;

public class ServerConfig {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> SUFFIX_PLAYERS = BUILDER
            .comment("JSON-formatted list of players and their suffixes. Format: {\"playerName\": \"suffix\"}")
            .define("suffixPlayers", "{}", ServerConfig::validate);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean validate(Object obj) {
        if (!(obj instanceof String str)) {
            return false;
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
            // Check if all values are strings
            return jsonObject.entrySet().stream().allMatch(element ->
                    element.getValue().isJsonPrimitive() && element.getValue().getAsJsonPrimitive().isString()
            );
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    public static JsonObject getSuffixPlayers() {
        String jsonString = SUFFIX_PLAYERS.get();
        if (jsonString == null || jsonString.isEmpty()) {
            return new JsonObject();
        }

        try {
            return JsonParser.parseString(jsonString).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            LOGGER.error("Invalid JSON format for suffixPlayers config: {}", jsonString, e);
            return new JsonObject();
        }
    }
}
