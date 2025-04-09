package net.iljg.wunkysenhancements.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> PROTECTION_CHARGES;
    public static final ForgeConfigSpec.ConfigValue<Integer> SHARPNESS_CHARGES;
    public static final ForgeConfigSpec.DoubleValue SHARPNESS_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> XP_REQUIREMENT;

    static {
        BUILDER.push("Wunky's Enhancements");

        PROTECTION_CHARGES = BUILDER.comment("Number of uses Protection grants. Default 250")
                .defineInRange("Protection uses", 250, 1, 1000);

        PROTECTION_AMOUNT = BUILDER.comment("Damage reduction; 0.1 being 10%, 1 being 100%. Default 0.1")
                .defineInRange("Damage reduction", 0.1, 0.1, 1);

        SHARPNESS_CHARGES = BUILDER.comment("Number of uses Sharpness grants. Default 250")
                .defineInRange("Sharpness uses", 250,1, 1000);

        SHARPNESS_DAMAGE = BUILDER.comment("Amount of damage Sharpness grants. Default 1.5")
                .defineInRange("Sharpness damage", 1.5,0, 5);

        XP_REQUIREMENT = BUILDER.comment("XP cost of enhancing gear. Default 55")
                .defineInRange("XP cost", 0, 0, 500);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}