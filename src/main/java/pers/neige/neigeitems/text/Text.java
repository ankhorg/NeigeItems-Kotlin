package pers.neige.neigeitems.text;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtList;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtString;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.impl.ConditionText;
import pers.neige.neigeitems.text.impl.ListText;
import pers.neige.neigeitems.text.impl.StringText;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.function.Function;

public abstract class Text {
    static final @NonNull Function<String, String> converterNull = String::valueOf;
    static final @NonNull Function<String, Nbt<?>> converterV12 = NbtString::valueOf;
    static final @NonNull Function<String, Nbt<?>> converterV16ToV20 = (text) -> NbtString.valueOf(TranslationUtils.fromStringToJSON(text));

    protected final @NonNull BaseActionManager manager;

    public Text(@NonNull BaseActionManager manager) {
        this.manager = manager;
    }

    public static Text compile(
            @NonNull BaseActionManager manager,
            @Nullable Object action
    ) {
        if (action instanceof String) {
            return new StringText(manager, (String) action);
        } else if (action instanceof List<?>) {
            return new ListText(manager, (List<?>) action);
        }
        val config = ConfigReader.parse(action);
        if (config == null) return manager.NULL_TEXT;
        return new ConditionText(manager, config);
    }

    public @NonNull <T, R extends List<T>> R getText(@NonNull R result, @NonNull BaseActionManager manager, @NonNull ActionContext context, Function<String, T> converter) {
        return result;
    }

    public @NonNull NbtList getLoreNbt(@NonNull BaseActionManager manager, @NonNull ActionContext context) {
        if (CbVersion.v1_20_R4.isSupport()) {
            throw new InvalidParameterException("1.20.5+版本, 你拿你妈了个逼的NBT形式Lore啊? 你Lore是NBT形式吗你就拿?");
        } else if (CbVersion.current() == CbVersion.v1_12_R1) {
            return getText(new NbtList(), manager, context, converterV12);
        } else if (CbVersion.v1_16_R3.isSupport()) {
            return getText(new NbtList(), manager, context, converterV16ToV20);
        } else {
            throw new InvalidParameterException("不支持1.13-1.16.4版本");
        }
    }
}
