package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.scores.RefPlayerTeam;

import java.util.Collection;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutScoreboardTeam", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefPacketPlayOutScoreboardTeam implements RefPacket<RefPacketListenerPlayOut> {
    public static final int METHOD_ADD = 0;
    public static final int METHOD_REMOVE = 1;
    public static final int METHOD_CHANGE = 2;
    public static final int METHOD_JOIN = 3;
    public static final int METHOD_LEAVE = 4;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;name:Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutScoreboardTeam;a:Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public String name;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;method:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutScoreboardTeam;i:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public int method;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;players:Ljava/util/Collection;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutScoreboardTeam;h:Ljava/util/Collection;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public Collection<String> entities;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutScoreboardTeam;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPacketPlayOutScoreboardTeam() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;createAddOrModifyPacket(Lnet/minecraft/world/scores/PlayerTeam;Z)Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native RefPacketPlayOutScoreboardTeam createAddOrModifyPacket(RefPlayerTeam team, boolean updatePlayers);
}
