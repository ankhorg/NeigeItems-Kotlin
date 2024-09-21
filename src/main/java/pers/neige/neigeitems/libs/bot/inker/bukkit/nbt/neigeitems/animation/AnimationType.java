package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.animation;

public enum AnimationType {
    SWING_MAIN_HAND(0),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRITICAL_EFFECT(4),
    MAGIC_CRITICAL_EFFECT(5);

    private final int value;

    AnimationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
