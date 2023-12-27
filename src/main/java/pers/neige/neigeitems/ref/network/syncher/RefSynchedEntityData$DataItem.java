package pers.neige.neigeitems.ref.network.syncher;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/syncher/DataWatcher$Item", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/DataWatcher$Item", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefSynchedEntityData$DataItem {
    @HandleBy(reference = "Lnet/minecraft/network/syncher/DataWatcher$Item;a()Lnet/minecraft/network/syncher/DataWatcherObject;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/DataWatcherObject;b()Lnet/minecraft/server/v1_12_R1/DataWatcherSerializer;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefEntityDataAccessor getAccessor();
}
