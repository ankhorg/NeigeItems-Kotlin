package pers.neige.neigeitems.item.action;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum ItemActionType {
    LEFT("left"),
    SHIFT_LEFT("shift_left"),
    RIGHT("right"),
    SHIFT_RIGHT("shift_right"),
    ALL("all"),
    SHIFT_ALL("shift_all"),
    EAT("eat"),
    DROP("drop"),
    PICK("pick"),
    CLICK("click"),
    BECLICKED("beclicked"),
    SHOOT_BOW("shoot_bow"),
    SHOOT_ARROW("shoot_arrow"),
    BLOCKING("blocking"),
    DAMAGE("damage"),
    KILL_HAND("kill_hand"),
    KILL_OFFHAND("kill_offhand"),
    KILL_HEAD("kill_head"),
    KILL_CHEST("kill_chest"),
    KILL_LEGS("kill_legs"),
    KILL_FEET("kill_feet"),
    BREAK_BLOCK("break_block"),
    TICK_HAND("tick_hand"),
    TICK_OFFHAND("tick_offhand"),
    TICK_HEAD("tick_head"),
    TICK_CHEST("tick_chest"),
    TICK_LEGS("tick_legs"),
    TICK_FEET("tick_feet"),
    TICK_0("tick_0"),
    TICK_1("tick_1"),
    TICK_2("tick_2"),
    TICK_3("tick_3"),
    TICK_4("tick_4"),
    TICK_5("tick_5"),
    TICK_6("tick_6"),
    TICK_7("tick_7"),
    TICK_8("tick_8"),
    TICK_9("tick_9"),
    TICK_10("tick_10"),
    TICK_11("tick_11"),
    TICK_12("tick_12"),
    TICK_13("tick_13"),
    TICK_14("tick_14"),
    TICK_15("tick_15"),
    TICK_16("tick_16"),
    TICK_17("tick_17"),
    TICK_18("tick_18"),
    TICK_19("tick_19"),
    TICK_20("tick_20"),
    TICK_21("tick_21"),
    TICK_22("tick_22"),
    TICK_23("tick_23"),
    TICK_24("tick_24"),
    TICK_25("tick_25"),
    TICK_26("tick_26"),
    TICK_27("tick_27"),
    TICK_28("tick_28"),
    TICK_29("tick_29"),
    TICK_30("tick_30"),
    TICK_31("tick_31"),
    TICK_32("tick_32"),
    TICK_33("tick_33"),
    TICK_34("tick_34"),
    TICK_35("tick_35"),
    TICK_36("tick_36"),
    TICK_37("tick_37"),
    TICK_38("tick_38"),
    TICK_39("tick_39"),
    TICK_40("tick_40");

    private static final Map<String, ItemActionType> typeToEnum = new HashMap<>();

    static {
        for (ItemActionType value : ItemActionType.values()) {
            typeToEnum.put(value.type, value);
        }
    }

    private final String type;

    ItemActionType(String type) {
        this.type = type;
    }

    @Nullable
    public static ItemActionType matchType(
            @Nullable String type
    ) {
        return typeToEnum.get(type);
    }

    public String getType() {
        return type;
    }
}
