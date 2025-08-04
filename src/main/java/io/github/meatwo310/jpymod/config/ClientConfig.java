package io.github.meatwo310.jpymod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue MODIFY_SHARED_ITEM = BUILDER
            .push("sharedItem")
            .comment("Whether to modify the item sharing feature of Quark")
            .define("modifySharedItem", false);
    public static final ForgeConfigSpec.DoubleValue SHIFT_SHARED_ITEM_X = BUILDER
            .comment("Shift the X position of the item shared by Quark")
            .defineInRange("shiftSharedItemX", 0, -Float.MAX_VALUE, Float.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue SHIFT_SHARED_ITEM_Y = BUILDER
            .comment("Shift the Y position of the item shared by Quark")
            .defineInRange("shiftSharedItemY", 0.0, -Float.MAX_VALUE, Float.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue SCALE_SHARED_ITEM = BUILDER
            .comment("Scale the item shared by Quark")
            .defineInRange("scaleSharedItem", 1.00, 0.0, Float.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
