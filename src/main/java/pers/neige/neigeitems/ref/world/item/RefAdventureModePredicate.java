package pers.neige.neigeitems.ref.world.item;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/item/AdventureModePredicate", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefAdventureModePredicate {
    @HandleBy(reference = "Lnet/minecraft/world/item/AdventureModePredicate;withTooltip(Z)Lnet/minecraft/world/item/AdventureModePredicate;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native RefAdventureModePredicate withTooltip(boolean showInTooltip);
}
