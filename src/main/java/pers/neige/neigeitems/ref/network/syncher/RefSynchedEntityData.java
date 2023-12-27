package pers.neige.neigeitems.ref.network.syncher;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityPlayer;

import java.util.List;

@HandleBy(reference = "net/minecraft/network/syncher/SynchedEntityData", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/DataWatcher", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefSynchedEntityData {
    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;<init>(Lnet/minecraft/world/entity/Entity;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/DataWatcher;<init>(Lnet/minecraft/server/v1_12_R1/Entity;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefSynchedEntityData(RefEntity trackedEntity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;refresh(Lnet/minecraft/server/level/ServerPlayer;)V", predicates = "craftbukkit_version:[v1_19_R3,)")
    public native void refresh(RefEntityPlayer to);

    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;packDirty()Ljava/util/List;", predicates = "craftbukkit_version:[v1_19_R3,)")
    public native List<RefSynchedEntityData$DataValue> packDirty();

    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;getNonDefaultValues()Ljava/util/List;", predicates = "craftbukkit_version:[v1_19_R3,)")
    public native List<RefSynchedEntityData$DataValue> getNonDefaultValues();

    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/DataWatcher;set(Lnet/minecraft/server/v1_12_R1/DataWatcherObject;Ljava/lang/Object;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native <T> void set(RefEntityDataAccessor<T> key, T value);

    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V", predicates = "craftbukkit_version:[v1_19_R3,)")
    public native <T> void set(RefEntityDataAccessor<T> key, T value, boolean force);

    @HandleBy(reference = "Lnet/minecraft/network/syncher/SynchedEntityData;define(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/DataWatcher;register(Lnet/minecraft/server/v1_12_R1/DataWatcherObject;Ljava/lang/Object;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native <T> void define(RefEntityDataAccessor<T> key, T value);
}
