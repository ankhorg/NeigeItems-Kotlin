package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.NbtType;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.*;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntityTypes;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.item.*;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.item.potion.RefPotionUtil;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.item.shield.RefTileEntityBanner;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.util.RefCraftMagicNumbers;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.lang.LocaleI18n;

public class TranslationUtils {
    /**
     * 根据物品获取翻译名.
     *
     * @param itemStack 待检测物品.
     * @return 翻译名.
     */
    @NotNull
    public static String getTranslationName(
            @NotNull ItemStack itemStack
    ) {
        RefNmsItemStack nmsItemStack;
        if ((Object) itemStack instanceof RefCraftItemStack) {
            nmsItemStack = ((RefCraftItemStack) (Object) itemStack).handle;
        } else {
            nmsItemStack = RefCraftItemStack.asNMSCopy(itemStack);
        }
        RefItem item = nmsItemStack.getItem();
        String result = null;
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
        if (CbVersion.v1_13_R1.isSupport()) {
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
     * 根据物品获取 TranslatableComponen.
     *
     * @param itemStack 待检测物品.
     * @return TranslatableComponen.
     */
    @NotNull
    public static BaseComponent getTranslatableComponent(
            @NotNull ItemStack itemStack
    ) {
        RefNmsItemStack nmsItemStack;
        if ((Object) itemStack instanceof RefCraftItemStack) {
            nmsItemStack = ((RefCraftItemStack) (Object) itemStack).handle;
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
     * 根据材质获取对应的 DescriptionId.
     * 在 1.12.2 版本, DescriptionId 与 "翻译键" 有很大的差别.
     * 而在 1.13+ 版本, 除头颅和成书外, DescriptionId 等同于翻译键.
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
