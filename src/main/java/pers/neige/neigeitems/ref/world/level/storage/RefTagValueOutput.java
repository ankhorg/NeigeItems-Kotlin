package pers.neige.neigeitems.ref.world.level.storage;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.util.RefProblemReporter;

@HandleBy(reference = "net/minecraft/world/level/storage/TagValueOutput", predicates = "craftbukkit_version:[v26_1,)")
public final class RefTagValueOutput implements RefValueOutput {
    @HandleBy(reference = "Lnet/minecraft/world/level/storage/TagValueOutput;createWithoutContext(Lnet/minecraft/util/ProblemReporter;)Lnet/minecraft/world/level/storage/TagValueOutput;", predicates = "craftbukkit_version:[v26_1,)")
    public static native RefTagValueOutput createWithoutContext(RefProblemReporter problemReporter);

    @HandleBy(reference = "Lnet/minecraft/world/level/storage/TagValueOutput;buildResult()Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v26_1,)")
    public native RefNbtTagCompound buildResult();
}
