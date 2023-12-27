package pers.neige.neigeitems.ref.network.syncher;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/syncher/EntityDataAccessor", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/DataWatcherObject", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefEntityDataAccessor<T> {
    @HandleBy(reference = "Lnet/minecraft/network/syncher/EntityDataAccessor;getId()I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/DataWatcherObject;a()I", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native int getId();
}
