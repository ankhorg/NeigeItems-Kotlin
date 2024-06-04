package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation;

import org.bukkit.Bukkit;

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
    v1_20_R3;

    private static final CbVersion CURRENT = valueOf(Bukkit.getServer().getClass().getName().split("\\.")[3]);

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
