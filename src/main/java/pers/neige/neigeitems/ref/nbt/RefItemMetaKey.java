package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "java/lang/Object", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefItemMetaKey {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;BUKKIT:Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public final String BUKKIT = null;

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem$ItemMetaKey;NBT:Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public final String NBT = null;
}
