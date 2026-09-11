package pers.neige.neigeitems.ref.world.level.storage;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.registry.RefHolderLookup$Provider;
import pers.neige.neigeitems.ref.util.RefProblemReporter;

@HandleBy(reference = "net/minecraft/world/level/storage/TagValueInput", predicates = "craftbukkit_version:[v26_1,)")
public final class RefTagValueInput {
    @HandleBy(reference = "Lnet/minecraft/world/level/storage/TagValueInput;create(Lnet/minecraft/util/ProblemReporter;Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/storage/ValueInput;", predicates = "craftbukkit_version:[v26_1,)")
    public static native RefValueInput create(
        RefProblemReporter problemReporter,
        RefHolderLookup$Provider holders,
        RefNbtTagCompound tag
    );
}
