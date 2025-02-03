package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.lang.LocaleI18n;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtType;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke.InvokeUtil;
import pers.neige.neigeitems.ref.chat.RefChatFormatting;
import pers.neige.neigeitems.ref.chat.RefChatSerializer;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.chat.RefCraftChatMessage;
import pers.neige.neigeitems.ref.core.component.RefDataComponentHolder;
import pers.neige.neigeitems.ref.core.component.RefDataComponents;
import pers.neige.neigeitems.ref.core.component.RefResolvableProfile;
import pers.neige.neigeitems.ref.entity.RefCraftEntity;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityTypes;
import pers.neige.neigeitems.ref.item.*;
import pers.neige.neigeitems.ref.item.potion.RefPotionUtil;
import pers.neige.neigeitems.ref.item.shield.RefTileEntityBanner;
import pers.neige.neigeitems.ref.nbt.*;
import pers.neige.neigeitems.ref.util.RefCraftMagicNumbers;

import java.util.HashMap;
import java.util.Map;

public class TranslationUtils {
    protected static final Map<String, RefChatFormatting> CHAT_COLORS = new HashMap<>();
    /**
     * 1.13+ 版本起, EntityTypes 类发生了一些巨大的变化.
     */
    private static final boolean NEW_ENTITY_TYPE_SUPPORT = CbVersion.v1_13_R1.isSupport();
    /**
     * 1.20.5+ 版本起, Mojang献祭了自己的亲妈, 换来了物品格式的改动.
     */
    private final static boolean MOJANG_MOTHER_DEAD = CbVersion.v1_20_R4.isSupport();

    static {
        CHAT_COLORS.put("BLACK", RefChatFormatting.BLACK);
        CHAT_COLORS.put("DARK_BLUE", RefChatFormatting.DARK_BLUE);
        CHAT_COLORS.put("DARK_GREEN", RefChatFormatting.DARK_GREEN);
        CHAT_COLORS.put("DARK_AQUA", RefChatFormatting.DARK_AQUA);
        CHAT_COLORS.put("DARK_RED", RefChatFormatting.DARK_RED);
        CHAT_COLORS.put("DARK_PURPLE", RefChatFormatting.DARK_PURPLE);
        CHAT_COLORS.put("GOLD", RefChatFormatting.GOLD);
        CHAT_COLORS.put("GRAY", RefChatFormatting.GRAY);
        CHAT_COLORS.put("DARK_GRAY", RefChatFormatting.DARK_GRAY);
        CHAT_COLORS.put("BLUE", RefChatFormatting.BLUE);
        CHAT_COLORS.put("GREEN", RefChatFormatting.GREEN);
        CHAT_COLORS.put("AQUA", RefChatFormatting.AQUA);
        CHAT_COLORS.put("RED", RefChatFormatting.RED);
        CHAT_COLORS.put("LIGHT_PURPLE", RefChatFormatting.LIGHT_PURPLE);
        CHAT_COLORS.put("YELLOW", RefChatFormatting.YELLOW);
        CHAT_COLORS.put("WHITE", RefChatFormatting.WHITE);
    }

    /**
     * 根据物品获取显示名, 无显示名则返回翻译名.
     *
     * @param itemStack 待获取物品.
     * @return 显示名或翻译名.
     */
    @NotNull
    public static String getDisplayOrTranslationName(
            @NotNull ItemStack itemStack
    ) {
        String displayName = getDisplayName(itemStack);
        return displayName == null ? TranslationUtils.getTranslationName(itemStack) : displayName;
    }

    /**
     * 根据物品获取显示名, 无显示名则返回翻译键.
     *
     * @param itemStack 待获取物品.
     * @return 显示名或翻译键.
     */
    @NotNull
    public static BaseComponent getDisplayOrTranslationComponent(
            @NotNull ItemStack itemStack
    ) {
        String displayName = getDisplayName(itemStack);
        return displayName == null ? TranslationUtils.getTranslationComponent(itemStack) : new TextComponent(displayName);
    }

    /**
     * 根据物品获取显示名, 无显示名则返回 null.
     *
     * @param itemStack 待获取物品.
     * @return 显示名.
     */
    @Nullable
    public static String getDisplayName(
            @NotNull ItemStack itemStack
    ) {
        if (itemStack instanceof RefCraftItemStack) {
            return getDisplayNameFromCraftItemStack(itemStack);
        } else {
            if (MOJANG_MOTHER_DEAD) {
                return getDisplayNameFromCraftItemStack(((RefBukkitItemStack) (Object) itemStack).craftDelegate);
            }
            ItemMeta itemMeta = InvokeUtil.getItemMeta(itemStack);
            if (itemMeta != null && itemMeta.hasDisplayName()) {
                return itemMeta.getDisplayName();
            }
        }
        return null;
    }

    /**
     * 根据CraftItemStack获取显示名, 无显示名或物品为org.bukkit.inventory.ItemStack则返回 null, 仅适用于1.20.4及以下版本.
     *
     * @param itemStack 待获取物品.
     * @return 显示名.
     */
    @Nullable
    public static String getDisplayNameFromCraftItemStack(@Nullable ItemStack itemStack) {
        if (!(itemStack instanceof RefCraftItemStack) || itemStack.getType() == Material.AIR) return null;
        if (MOJANG_MOTHER_DEAD) {
            RefComponent name = ((RefDataComponentHolder) (Object) ((RefCraftItemStack) itemStack).handle).get(RefDataComponents.CUSTOM_NAME);
            return name == null ? null : name.getString();
        } else {
            RefNbtTagCompound tag = ((RefCraftItemStack) itemStack).handle.getTag();
            if (tag == null) return null;
            RefNbtBase display = tag.get("display");
            if (!(display instanceof RefNbtTagCompound)) return null;
            RefNbtBase tagName = ((RefNbtTagCompound) display).get("Name");
            if (!(tagName instanceof RefNbtTagString)) return null;
            String rawName = tagName.asString();
            if (CbVersion.current() == CbVersion.v1_12_R1) {
                return rawName;
            } else if (CbVersion.v1_15_R1.isSupport()) {
                return RefCraftChatMessage.fromComponent(RefChatSerializer.fromJson(rawName));
            } else {
                return RefCraftChatMessage.fromComponent(RefChatSerializer.fromJson(rawName), RefChatFormatting.WHITE);
            }
        }
    }

    /**
     * 将高版本json文本转化为传统文本(1.12.2不进行转换).
     *
     * @param json 待转换json.
     * @return 传统文本.
     */
    @NotNull
    public static String toLegacyText(
            @NotNull String json
    ) {
        if (CbVersion.current() == CbVersion.v1_12_R1) {
            return json;
        } else if (CbVersion.v1_17_R1.isSupport()) {
            return RefCraftChatMessage.fromJSONComponent(json);
        } else if (CbVersion.v1_15_R1.isSupport()) {
            return RefCraftChatMessage.fromComponent(RefChatSerializer.fromJson(json));
        } else {
            return RefCraftChatMessage.fromComponent(RefChatSerializer.fromJson(json), RefChatFormatting.WHITE);
        }
    }

    /**
     * 将传统文本转化为高版本json文本(1.12.2-1.16.4不进行转换).
     *
     * @param json 待转换json.
     * @return 传统文本.
     */
    @NotNull
    public static String toJsonText(
            @NotNull String json
    ) {
        if (CbVersion.v1_16_R3.isSupport()) {
            return fromStringToJSON(json);
        } else {
            return json;
        }
    }

    /**
     * 适用于1.16.5+版本, 将高版本json文本转化为传统文本.
     *
     * @param jsonMessage 待转换json.
     * @return 传统文本.
     */
    @Nullable
    public static String fromJSONComponent(
            @Nullable String jsonMessage
    ) {
        if (jsonMessage == null) return null;
        if (CbVersion.v1_16_R3.isSupport())
            return RefCraftChatMessage.fromJSONComponent(jsonMessage);
        return null;
    }

    /**
     * 适用于1.16.5+版本, 将传统文本转化为高版本json文本.
     * keepNewlines 默认为 false.
     *
     * @param message 待转换传统文本.
     * @return 传统文本.
     */
    public static String fromStringToJSON(String message) {
        if (message == null) return null;
        if (CbVersion.v1_16_R3.isSupport())
            return RefCraftChatMessage.fromStringToJSON(message);
        return null;
    }

    /**
     * 适用于1.16.5+版本, 将传统文本转化为高版本json文本.
     *
     * @param message      待转换传统文本.
     * @param keepNewlines 此选项为 true 时, 将把文本中的换行符转化为 \n 文本, 为 false 时, 将于换行符处直接断开停止识别.
     * @return 传统文本.
     */
    public static String fromStringToJSON(String message, boolean keepNewlines) {
        if (message == null) return null;
        if (CbVersion.v1_16_R3.isSupport())
            return RefCraftChatMessage.fromStringToJSON(message, keepNewlines);
        return null;
    }

    /**
     * 检测物品是否包含自定义显示名.
     *
     * @param itemStack 待检测物品.
     * @return 是否包含自定义显示名.
     */
    public static boolean hasDisplayName(
            @NotNull ItemStack itemStack
    ) {
        if (itemStack instanceof RefCraftItemStack) {
            if (itemStack.getType() != Material.AIR) {
                RefNbtTagCompound tag = ((RefCraftItemStack) itemStack).handle.getTag();
                if (tag != null) {
                    RefNbtBase display = tag.get("display");
                    if (display instanceof RefNbtTagCompound) {
                        RefNbtBase tagName = ((RefNbtTagCompound) display).get("Name");
                        return tagName instanceof RefNbtTagString;
                    }
                }
            }
        } else {
            ItemMeta itemMeta = NbtUtils.getItemMeta(itemStack);
            return itemMeta != null && itemMeta.hasDisplayName();
        }
        return false;
    }

    /**
     * 根据物品获取翻译名.
     *
     * @param itemStack 待获取物品.
     * @return 翻译名.
     */
    @NotNull
    public static String getTranslationName(
            @NotNull ItemStack itemStack
    ) {
        RefNmsItemStack nmsItemStack;
        if (itemStack instanceof RefCraftItemStack && itemStack.getType() != Material.AIR) {
            nmsItemStack = ((RefCraftItemStack) itemStack).handle;
        } else {
            if (MOJANG_MOTHER_DEAD) {
                nmsItemStack = ((RefCraftItemStack) ((RefBukkitItemStack) (Object) itemStack).craftDelegate).handle;
            } else {
                nmsItemStack = RefCraftItemStack.asNMSCopy(itemStack);
            }
        }
        RefItem item = nmsItemStack.getItem();
        String result = null;
        // 玩家头颅和成书的翻译键, 全版本都需要特殊处理
        // 玩家头颅
        if (item instanceof RefItemSkull) {
            if (MOJANG_MOTHER_DEAD) {
                RefResolvableProfile resolvableProfile = nmsItemStack.components.get(RefDataComponents.PROFILE);
                if (resolvableProfile != null && resolvableProfile.name().isPresent()) {
                    result = LocaleI18n.translate(item.getDescriptionId() + ".named").replaceFirst("%s", resolvableProfile.name().get());
                }
            } else if (
                // 1.13+ 肯定是玩家头颅, 1.12.2 需要检测损伤值
                    (CbVersion.v1_13_R1.isSupport() || itemStack.getDurability() == 3)
                            // NBT 检测
                            && nmsItemStack.hasTag()
            ) {
                RefNbtTagCompound tag = nmsItemStack.getTag();
                RefNbtBase skullOwnerTag = tag.get("SkullOwner");
                String ownerName = null;
                if (skullOwnerTag instanceof RefNbtTagString) {
                    ownerName = skullOwnerTag.asString();
                } else if (skullOwnerTag instanceof RefNbtTagCompound) {
                    RefNbtBase nameTag = ((RefNbtTagCompound) skullOwnerTag).get("Name");
                    if (nameTag instanceof RefNbtTagString) {
                        ownerName = nameTag.asString();
                    }
                }
                if (ownerName != null) {
                    String temp;
                    // 1.13+ 具名玩家头颅的翻译键
                    if (CbVersion.v1_13_R1.isSupport()) {
                        temp = LocaleI18n.translate(item.getDescriptionId() + ".named");
                        // 1.12.2 具名玩家头颅的翻译键
                    } else {
                        temp = LocaleI18n.translate("item.skull.player.name");
                    }
                    result = temp.replaceFirst("%s", ownerName);
                }
            }
            // 成书
        } else if (item instanceof RefItemWrittenBook) {
            RefNbtTagCompound tag = nmsItemStack.getTag();
            if (tag != null) {
                RefNbtBase title = tag.get("title");
                if (title instanceof RefNbtTagString) {
                    result = title.asString();
                }
            }
        }
        if (MOJANG_MOTHER_DEAD) {
            if (item instanceof RefItemCompass) {
                if (nmsItemStack.components.has(RefDataComponents.LODESTONE_TRACKER)) {
                    result = LocaleI18n.translate("item.minecraft.lodestone_compass");
                }
            }
            if (result == null) {
                String translationKey = item.getDescriptionId();
                result = LocaleI18n.translate(translationKey);
            }
        } else if (CbVersion.v1_13_R1.isSupport()) {
            if (result == null) {
                String translationKey = item.getDescriptionId(nmsItemStack);
                result = LocaleI18n.translate(translationKey);
            }
            // 1.12.2 获取翻译键需要经历更多的痛苦折磨
        } else {
            // 旗帜
            if (item instanceof RefItemBanner) {
                RefEnumColor color = RefItemBanner.getColor(nmsItemStack);
                String translationKey = "item.banner." + color.getTranslationKey() + ".name";
                result = LocaleI18n.translate(translationKey);
                // 喷溅药水
            } else if (item instanceof RefItemSplashPotion) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("splash_potion.effect.");
                result = LocaleI18n.translate(translationKey);
                // 滞留药水
            } else if (item instanceof RefItemLingeringPotion) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("lingering_potion.effect.");
                result = LocaleI18n.translate(translationKey);
                // 普通药水
            } else if (item instanceof RefItemPotion) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("potion.effect.");
                result = LocaleI18n.translate(translationKey);
                // 药水箭
            } else if (item instanceof RefItemTippedArrow) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("tipped_arrow.effect.");
                result = LocaleI18n.translate(translationKey);
                // 刷怪蛋
            } else if (item instanceof RefItemMonsterEgg) {
                String basicName = LocaleI18n.translate(item.getDescriptionId(nmsItemStack) + ".name");
                String entityTranslationKey = RefEntityTypes.getTranslationKey(RefItemMonsterEgg.getEntityType(nmsItemStack));
                if (entityTranslationKey == null) {
                    entityTranslationKey = "generic";
                }
                result = basicName + " " + LocaleI18n.translate("entity." + entityTranslationKey + ".name");
                // 盾牌
            } else if (item instanceof RefItemShield) {
                RefNbtTagCompound tag = nmsItemStack.getTag();
                if (tag != null && tag.hasKeyOfType("BlockEntityTag", NbtType.TAG_COMPOUND)) {
                    RefEnumColor color = RefTileEntityBanner.getColor(nmsItemStack);
                    result = LocaleI18n.translate("item.shield." + color.getTranslationKey() + ".name");
                } else {
                    result = LocaleI18n.translate("item.shield.name");
                }
            }
            if (result == null) {
                String translationKey = item.getDescriptionId(nmsItemStack) + ".name";
                result = LocaleI18n.translate(translationKey);
            }
        }
        return result;
    }

    /**
     * 根据物品获取包含翻译键的 BaseComponent.
     *
     * @param itemStack 待获取物品.
     * @return 包含翻译键的 BaseComponent.
     */
    @NotNull
    public static BaseComponent getTranslationComponent(
            @NotNull ItemStack itemStack
    ) {
        RefNmsItemStack nmsItemStack;
        if (itemStack instanceof RefCraftItemStack && itemStack.getType() != Material.AIR) {
            nmsItemStack = ((RefCraftItemStack) itemStack).handle;
        } else {
            nmsItemStack = RefCraftItemStack.asNMSCopy(itemStack);
        }
        RefItem item = nmsItemStack.getItem();
        BaseComponent result = null;
        // 玩家头颅和成书的翻译键, 全版本都需要特殊处理
        // 玩家头颅
        if (item instanceof RefItemSkull) {
            if (
                // 1.13+ 肯定是玩家头颅, 1.12.2 需要检测损伤值
                    (CbVersion.v1_13_R1.isSupport() || itemStack.getDurability() == 3)
                            // NBT 检测
                            && nmsItemStack.hasTag()
            ) {
                RefNbtTagCompound tag = nmsItemStack.getTag();
                RefNbtBase skullOwnerTag = tag.get("SkullOwner");
                String ownerName = null;
                if (skullOwnerTag instanceof RefNbtTagString) {
                    ownerName = skullOwnerTag.asString();
                } else if (skullOwnerTag instanceof RefNbtTagCompound) {
                    RefNbtBase nameTag = ((RefNbtTagCompound) skullOwnerTag).get("Name");
                    if (nameTag instanceof RefNbtTagString) {
                        ownerName = nameTag.asString();
                    }
                }
                if (ownerName != null) {
                    // 1.13+ 具名玩家头颅的翻译键
                    if (CbVersion.v1_13_R1.isSupport()) {
                        result = new TranslatableComponent(item.getDescriptionId() + ".named", ownerName);
                        // 1.12.2 具名玩家头颅的翻译键
                    } else {
                        result = new TranslatableComponent("item.skull.player.name", ownerName);
                    }
                }
            }
            // 成书
        } else if (item instanceof RefItemWrittenBook) {
            RefNbtTagCompound tag = nmsItemStack.getTag();
            if (tag != null) {
                RefNbtBase title = tag.get("title");
                if (title instanceof RefNbtTagString) {
                    result = new TextComponent(title.asString());
                }
            }
        }
        if (CbVersion.v1_13_R1.isSupport()) {
            if (result == null) {
                String translationKey = item.getDescriptionId(nmsItemStack);
                result = new TranslatableComponent(translationKey);
            }
            // 1.12.2 获取翻译键需要经历更多的痛苦折磨
        } else {
            // 旗帜
            if (item instanceof RefItemBanner) {
                RefEnumColor color = RefItemBanner.getColor(nmsItemStack);
                String translationKey = "item.banner." + color.getTranslationKey() + ".name";
                result = new TranslatableComponent(translationKey);
                // 喷溅药水
            } else if (item instanceof RefItemSplashPotion) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("splash_potion.effect.");
                result = new TranslatableComponent(translationKey);
                // 滞留药水
            } else if (item instanceof RefItemLingeringPotion) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("lingering_potion.effect.");
                result = new TranslatableComponent(translationKey);
                // 普通药水
            } else if (item instanceof RefItemPotion) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("potion.effect.");
                result = new TranslatableComponent(translationKey);
                // 药水箭
            } else if (item instanceof RefItemTippedArrow) {
                String translationKey = RefPotionUtil.getPotionRegistry(nmsItemStack).getTranslationKey("tipped_arrow.effect.");
                result = new TranslatableComponent(translationKey);
                // 刷怪蛋
            } else if (item instanceof RefItemMonsterEgg) {
                BaseComponent basicName = new TranslatableComponent(item.getDescriptionId(nmsItemStack) + ".name");
                String entityTranslationKey = RefEntityTypes.getTranslationKey(RefItemMonsterEgg.getEntityType(nmsItemStack));
                if (entityTranslationKey == null) {
                    entityTranslationKey = "generic";
                }
                basicName.addExtra(" ");
                basicName.addExtra(new TranslatableComponent("entity." + entityTranslationKey + ".name"));
                result = basicName;
                // 盾牌
            } else if (item instanceof RefItemShield) {
                RefNbtTagCompound tag = nmsItemStack.getTag();
                if (tag != null && tag.hasKeyOfType("BlockEntityTag", NbtType.TAG_COMPOUND)) {
                    RefEnumColor color = RefTileEntityBanner.getColor(nmsItemStack);
                    result = new TranslatableComponent("item.shield." + color.getTranslationKey() + ".name");
                } else {
                    result = new TranslatableComponent("item.shield.name");
                }
            }
            if (result == null) {
                String translationKey = item.getDescriptionId(nmsItemStack) + ".name";
                result = new TranslatableComponent(translationKey);
            }
        }
        return result;
    }

    /**
     * 根据实体获取显示名, 无显示名则返回翻译名.
     *
     * @param entity 待获取实体.
     * @return 显示名或翻译名.
     */
    @NotNull
    public static String getDisplayOrTranslationName(
            @NotNull Entity entity
    ) {
        String name = entity.getCustomName();
        if (name != null) {
            return name;
        }
        return getTranslationName(entity);
    }

    /**
     * 根据实体获取显示名, 无显示名则返回翻译键.
     *
     * @param entity 待获取实体.
     * @return 显示名或翻译键.
     */
    @NotNull
    public static BaseComponent getDisplayOrTranslationComponent(
            @NotNull Entity entity
    ) {
        String name = entity.getCustomName();
        if (name != null) {
            return new TextComponent(name);
        }
        return getTranslationComponent(entity);
    }

    /**
     * 根据实体获取翻译名.
     *
     * @param entity 待获取实体.
     * @return 翻译名.
     */
    @NotNull
    public static String getTranslationName(
            @NotNull Entity entity
    ) {
        String descriptionId = getDescriptionId(entity);
        if (descriptionId != null) {
            return LocaleI18n.translate(descriptionId);
        } else {
            return entity.getName();
        }
    }

    /**
     * 根据实体获取翻译键.
     *
     * @param entity 待获取实体.
     * @return 翻译键.
     */
    @NotNull
    public static BaseComponent getTranslationComponent(
            @NotNull Entity entity
    ) {
        String descriptionId = getDescriptionId(entity);
        if (descriptionId != null) {
            return new TranslatableComponent(descriptionId);
        } else {
            return new TextComponent(entity.getName());
        }
    }

    /**
     * 根据实体获取对应的 DescriptionId.
     *
     * @param entity 待检测实体.
     * @return DescriptionId.
     */
    @Nullable
    public static String getDescriptionId(
            @NotNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            if (NEW_ENTITY_TYPE_SUPPORT) {
                return nmsEntity.getType().getDescriptionId();
            } else {
                return "entity." + RefEntityTypes.getTranslationKey(RefEntityTypes.getMinecraftKey(nmsEntity)) + ".name";
            }
        }
        return null;
    }

    /**
     * 根据材质获取对应的 DescriptionId.
     * DescriptionId 与 "翻译键" 有很大的差别, 二者概念不可随意混淆.
     *
     * @param material 待检测材质.
     * @return DescriptionId.
     */
    @Nullable
    public static String getDescriptionId(
            @NotNull Material material
    ) {
        RefItem item = RefCraftMagicNumbers.getItem(material);
        if (item == null) return null;
        return item.getDescriptionId();
    }
}
