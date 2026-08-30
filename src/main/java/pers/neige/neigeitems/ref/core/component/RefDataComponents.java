package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.registry.RefRegistry;
import pers.neige.neigeitems.ref.world.item.component.RefCustomData;

@HandleBy(reference = "net/minecraft/core/component/DataComponents", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefDataComponents {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CUSTOM_DATA:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefCustomData> CUSTOM_DATA = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MAX_STACK_SIZE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> MAX_STACK_SIZE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MAX_DAMAGE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> MAX_DAMAGE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;DAMAGE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> DAMAGE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;UNBREAKABLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> UNBREAKABLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CUSTOM_NAME:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefComponent> CUSTOM_NAME = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ITEM_NAME:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefComponent> ITEM_NAME = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ITEM_MODEL:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> ITEM_MODEL = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;LORE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefItemLore> LORE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;RARITY:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> RARITY = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ENCHANTMENTS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> ENCHANTMENTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CAN_PLACE_ON:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CAN_PLACE_ON = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CAN_BREAK:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CAN_BREAK = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ATTRIBUTE_MODIFIERS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> ATTRIBUTE_MODIFIERS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CUSTOM_MODEL_DATA:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefCustomModelData> CUSTOM_MODEL_DATA = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;HIDE_ADDITIONAL_TOOLTIP:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public static final RefDataComponentType<?> HIDE_ADDITIONAL_TOOLTIP = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;HIDE_TOOLTIP:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public static final RefDataComponentType<?> HIDE_TOOLTIP = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TOOLTIP_DISPLAY:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> TOOLTIP_DISPLAY = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;REPAIR_COST:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> REPAIR_COST = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CREATIVE_SLOT_LOCK:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CREATIVE_SLOT_LOCK = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ENCHANTMENT_GLINT_OVERRIDE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> ENCHANTMENT_GLINT_OVERRIDE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;INTANGIBLE_PROJECTILE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> INTANGIBLE_PROJECTILE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;FOOD:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> FOOD = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CONSUMABLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CONSUMABLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;USE_REMAINDER:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> USE_REMAINDER = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;USE_COOLDOWN:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> USE_COOLDOWN = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;DAMAGE_RESISTANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> DAMAGE_RESISTANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TOOL:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> TOOL = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;WEAPON:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> WEAPON = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ENCHANTABLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> ENCHANTABLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ATTACK_RANGE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> ATTACK_RANGE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;EQUIPPABLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> EQUIPPABLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;REPAIRABLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> REPAIRABLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;GLIDER:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> GLIDER = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TOOLTIP_STYLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> TOOLTIP_STYLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;DEATH_PROTECTION:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> DEATH_PROTECTION = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BLOCKS_ATTACKS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> BLOCKS_ATTACKS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PIERCING_WEAPON:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> PIERCING_WEAPON = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;KINETIC_WEAPON:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> KINETIC_WEAPON = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;SWING_ANIMATION:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> SWING_ANIMATION = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;STORED_ENCHANTMENTS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> STORED_ENCHANTMENTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;DYED_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> DYED_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MAP_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> MAP_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MAP_ID:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> MAP_ID = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MAP_DECORATIONS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> MAP_DECORATIONS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MAP_POST_PROCESSING:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> MAP_POST_PROCESSING = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CHARGED_PROJECTILES:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CHARGED_PROJECTILES = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BUNDLE_CONTENTS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BUNDLE_CONTENTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;POTION_CONTENTS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> POTION_CONTENTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;POTION_DURATION_SCALE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> POTION_DURATION_SCALE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;SUSPICIOUS_STEW_EFFECTS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> SUSPICIOUS_STEW_EFFECTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;WRITABLE_BOOK_CONTENT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> WRITABLE_BOOK_CONTENT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;WRITTEN_BOOK_CONTENT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> WRITTEN_BOOK_CONTENT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TRIM:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> TRIM = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;DEBUG_STICK_STATE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> DEBUG_STICK_STATE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ENTITY_DATA:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> ENTITY_DATA = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BUCKET_ENTITY_DATA:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BUCKET_ENTITY_DATA = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BLOCK_ENTITY_DATA:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BLOCK_ENTITY_DATA = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;INSTRUMENT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> INSTRUMENT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PROVIDES_TRIM_MATERIAL:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> PROVIDES_TRIM_MATERIAL = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;OMINOUS_BOTTLE_AMPLIFIER:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> OMINOUS_BOTTLE_AMPLIFIER = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;JUKEBOX_PLAYABLE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> JUKEBOX_PLAYABLE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PROVIDES_BANNER_PATTERNS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> PROVIDES_BANNER_PATTERNS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;RECIPES:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> RECIPES = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;LODESTONE_TRACKER:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> LODESTONE_TRACKER = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;FIREWORK_EXPLOSION:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> FIREWORK_EXPLOSION = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;FIREWORKS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> FIREWORKS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PROFILE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefResolvableProfile> PROFILE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;NOTE_BLOCK_SOUND:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> NOTE_BLOCK_SOUND = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BANNER_PATTERNS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BANNER_PATTERNS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BASE_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BASE_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;POT_DECORATIONS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> POT_DECORATIONS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CONTAINER:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CONTAINER = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BLOCK_STATE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BLOCK_STATE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BEES:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> BEES = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;LOCK:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> LOCK = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CONTAINER_LOOT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<?> CONTAINER_LOOT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;USE_EFFECTS:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> USE_EFFECTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MINIMUM_ATTACK_CHARGE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> MINIMUM_ATTACK_CHARGE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;DAMAGE_TYPE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> DAMAGE_TYPE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;BREAK_SOUND:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> BREAK_SOUND = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;VILLAGER_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> VILLAGER_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;WOLF_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> WOLF_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;WOLF_SOUND_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> WOLF_SOUND_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;WOLF_COLLAR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> WOLF_COLLAR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;FOX_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> FOX_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;SALMON_SIZE:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> SALMON_SIZE = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PARROT_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> PARROT_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TROPICAL_FISH_PATTERN:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> TROPICAL_FISH_PATTERN = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TROPICAL_FISH_BASE_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> TROPICAL_FISH_BASE_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;TROPICAL_FISH_PATTERN_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> TROPICAL_FISH_PATTERN_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;MOOSHROOM_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> MOOSHROOM_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;RABBIT_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> RABBIT_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PIG_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> PIG_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;COW_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> COW_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CHICKEN_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> CHICKEN_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;ZOMBIE_NAUTILUS_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R7,)")
    public static final RefDataComponentType<?> ZOMBIE_NAUTILUS_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;FROG_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> FROG_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;HORSE_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> HORSE_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;PAINTING_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> PAINTING_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;LLAMA_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> LLAMA_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;AXOLOTL_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> AXOLOTL_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CAT_VARIANT:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> CAT_VARIANT = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CAT_COLLAR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> CAT_COLLAR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;SHEEP_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> SHEEP_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;SHULKER_COLOR:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentType<?> SHULKER_COLOR = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;COMMON_ITEM_COMPONENTS:Lnet/minecraft/core/component/DataComponentMap;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentMap COMMON_ITEM_COMPONENTS = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;bootstrap(Lnet/minecraft/core/Registry;)Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static native RefDataComponentType<?> bootstrap(RefRegistry<RefDataComponentType<?>> registry);
}
