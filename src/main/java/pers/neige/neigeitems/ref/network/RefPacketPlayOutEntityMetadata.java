package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData$DataItem;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData$DataValue;

import java.util.List;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundSetEntityDataPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntityMetadata", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefPacketPlayOutEntityMetadata implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;id:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityMetadata;a:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public int id;

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/PacketPlayOutEntityMetadata;b:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,v1_19_R3)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityMetadata;b:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public List<RefSynchedEntityData$DataItem> packedItems0;


    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;packedItems:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_19_R3,)")
    public List<RefSynchedEntityData$DataValue> packedItems1;

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;<init>(ILnet/minecraft/network/syncher/SynchedEntityData;Z)V", predicates = "craftbukkit_version:[v1_17_R1,v1_19_R3)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityMetadata;<init>(ILnet/minecraft/server/v1_12_R1/DataWatcher;Z)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPacketPlayOutEntityMetadata(int id, RefSynchedEntityData tracker, boolean forceUpdateAll) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;<init>(ILjava/util/List;)V", predicates = "craftbukkit_version:[v1_19_R3,)")
    public RefPacketPlayOutEntityMetadata(int id, List<RefSynchedEntityData$DataValue> list) {
        throw new UnsupportedOperationException();
    }
}
