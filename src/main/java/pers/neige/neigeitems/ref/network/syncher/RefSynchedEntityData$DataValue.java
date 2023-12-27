package pers.neige.neigeitems.ref.network.syncher;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/syncher/DataWatcher$b", predicates = "craftbukkit_version:[v1_19_R3,)")
public class RefSynchedEntityData$DataValue {
    @HandleBy(reference = "Lnet/minecraft/network/syncher/DataWatcher$b;a()I", predicates = "craftbukkit_version:[v1_19_R3,)")
    public native int id();
}
