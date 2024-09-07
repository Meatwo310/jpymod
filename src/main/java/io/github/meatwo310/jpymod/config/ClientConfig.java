package io.github.meatwo310.jpymod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue SHIFT_SHARED_ITEM_X = BUILDER
            .comment("Shift the X position of the item shared by Quark")
            .defineInRange("shiftSharedItemX", 1, -Float.MAX_VALUE, Float.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue SHIFT_SHARED_ITEM_Y = BUILDER
            .comment("Shift the Y position of the item shared by Quark")
            .defineInRange("shiftSharedItemY", 9.0, -Float.MAX_VALUE, Float.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue SCALE_SHARED_ITEM = BUILDER
            .comment("Scale the item shared by Quark")
            .defineInRange("scaleSharedItem", 1.25, 0.0, Float.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
