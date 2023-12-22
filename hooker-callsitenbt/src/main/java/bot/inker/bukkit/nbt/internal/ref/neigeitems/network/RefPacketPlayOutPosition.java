package pers.neige.neigeitems.internal.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundPlayerPositionPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutPosition", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefPacketPlayOutPosition implements RefPacket<RefPacketListenerPlayOut> {
}
