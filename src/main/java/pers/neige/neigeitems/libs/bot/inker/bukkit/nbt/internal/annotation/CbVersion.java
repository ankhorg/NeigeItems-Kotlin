package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation;

import org.inksnow.ankhinvoke.bukkit.util.CraftBukkitVersion;

public enum CbVersion {
    ALL,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3,
    v1_20_R1,
    v1_20_R2,
    v1_20_R3,
    v1_20_R4,
    v1_21_R1,
    v1_21_R2,
    v1_21_R3,
    v1_21_R4,
    v1_21_R5,
    v1_21_R6,
    v1_21_R7;

    private static final CbVersion CURRENT =
        CraftBukkitVersion.current() != CraftBukkitVersion.ALL
            ? valueOf(CraftBukkitVersion.current().name())
            : CbVersion.values()[CbVersion.values().length - 1];

    public static CbVersion current() {
        return CURRENT;
    }

    public boolean isSupport() {
        return current().ordinal() >= ordinal();
    }

    public boolean isUpTo() {
        return current().ordinal() <= ordinal();
    }
}
