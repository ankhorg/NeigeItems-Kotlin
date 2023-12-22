package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/Packet", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Packet", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public interface RefPacket<T extends RefPacketListener> {
}
