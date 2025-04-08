package net.iljg.wunkysenhancements.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> PROTECTION_CHARGES;
    public static final ForgeConfigSpec.ConfigValue<Integer> SHARPNESS_CHARGES;
    public static final ForgeConfigSpec.DoubleValue SHARPNESS_DAMAGE;

    static {
        BUILDER.push("Wunky's Enhancements");

        PROTECTION_CHARGES = BUILDER.comment("Number of uses Protection grants. Default 200")
                .defineInRange("Protection uses", 200, 1, 1000);

        SHARPNESS_CHARGES = BUILDER.comment("Number of uses Sharpness grants. Default 200")
                .defineInRange("Sharpness uses", 200,1, 1000);

        SHARPNESS_DAMAGE = BUILDER.comment("Amount of damage Sharpness grants. Default 1.5")
                .defineInRange("Sharpness damage", 1.5,0f, 5f);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}