package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Map;
import java.util.Set;

@HandleBy(reference = "org/bukkit/inventory/meta/ItemMeta", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftMetaItem {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;NAME:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Name", "display-name" 1.12
    public static final RefItemMetaKey NAME = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;LOCNAME:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "LocName", "loc-name" 1.12
    public static final RefItemMetaKey LOCNAME = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;DISPLAY:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "display" 1.12
    public static final RefItemMetaKey DISPLAY = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;LORE:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Lore", "lore" 1.12
    public static final RefItemMetaKey LORE = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_14_R1/inventory/CraftMetaItem;CUSTOM_MODEL_DATA:Lorg/bukkit/craftbukkit/v1_14_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_20_R3)")
    // "CustomModelData", "custom-model-data" 1.14
    public static final RefItemMetaKey CUSTOM_MODEL_DATA = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ENCHANTMENTS:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Enchantments", "enchants" 1.13
    // "ench", "enchants" 1.12
    public static final RefItemMetaKey ENCHANTMENTS = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ENCHANTMENTS_ID:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "id" 1.12
    public static final RefItemMetaKey ENCHANTMENTS_ID = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ENCHANTMENTS_LVL:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "lvl" 1.12
    public static final RefItemMetaKey ENCHANTMENTS_LVL = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;REPAIR:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "RepairCost", "repair-cost" 1.12
    public static final RefItemMetaKey REPAIR = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "AttributeModifiers", "attribute-modifiers" 1.13
    // "AttributeModifiers" 1.12
    public static final RefItemMetaKey ATTRIBUTES = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES_IDENTIFIER:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "AttributeName" 1.12
    public static final RefItemMetaKey ATTRIBUTES_IDENTIFIER = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES_NAME:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Name" 1.12
    public static final RefItemMetaKey ATTRIBUTES_NAME = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES_VALUE:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Amount" 1.12
    public static final RefItemMetaKey ATTRIBUTES_VALUE = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES_TYPE:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Operation" 1.12
    public static final RefItemMetaKey ATTRIBUTES_TYPE = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES_UUID_HIGH:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "UUIDMost" 1.12
    public static final RefItemMetaKey ATTRIBUTES_UUID_HIGH = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;ATTRIBUTES_UUID_LOW:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "UUIDLeast" 1.12
    public static final RefItemMetaKey ATTRIBUTES_UUID_LOW = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_13_R1/inventory/CraftMetaItem;ATTRIBUTES_SLOT:Lorg/bukkit/craftbukkit/v1_13_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_20_R3)")
    // "Slot" 1.13
    public static final RefItemMetaKey ATTRIBUTES_SLOT = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;HIDEFLAGS:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "HideFlags", "ItemFlags" 1.12
    public static final RefItemMetaKey HIDEFLAGS = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;UNBREAKABLE:Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_20_R3)")
    // "Unbreakable" 1.12
    public static final RefItemMetaKey UNBREAKABLE = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_13_R1/inventory/CraftMetaItem;DAMAGE:Lorg/bukkit/craftbukkit/v1_13_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_20_R3)")
    // "Damage" 1.13
    public static final RefItemMetaKey DAMAGE = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_14_R1/inventory/CraftMetaItem;BLOCK_DATA:Lorg/bukkit/craftbukkit/v1_14_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_20_R3)")
    // "BlockStateTag" 1.14
    public static final RefItemMetaKey BLOCK_DATA = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_13_R1/inventory/CraftMetaItem;BUKKIT_CUSTOM_TAG:Lorg/bukkit/craftbukkit/v1_13_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_20_R3)")
    // "PublicBukkitValues" 1.13
    public static final RefItemMetaKey BUKKIT_CUSTOM_TAG = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/CAN_DESTROY/inventory/CraftMetaItem;CAN_DESTROY:Lorg/bukkit/craftbukkit/v1_18_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_18_R1,v1_20_R3)")
    // "CanDestroy" 1.18
    public static final RefItemMetaKey CAN_DESTROY = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/CAN_DESTROY/inventory/CraftMetaItem;CAN_PLACE_ON:Lorg/bukkit/craftbukkit/v1_18_R1/inventory/CraftMetaItem$ItemMetaKey;", useAccessor = true, predicates = "craftbukkit_version:[v1_18_R1,v1_20_R3)")
    // "CanPlaceOn" 1.18
    public static final RefItemMetaKey CAN_PLACE_ON = null;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;HANDLED_TAGS:Ljava/util/Set;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public static Set<String> HANDLED_TAGS;
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;unhandledTags:Ljava/util/Map;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public Map<String, RefNbtBase> unhandledTags;

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/inventory/CraftMetaItem;applyToItem(Lnet/minecraft/nbt/CompoundTag;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem;applyToItem(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void applyToItem(RefNbtTagCompound itemTag);
}
