package pers.neige.neigeitems.ref.network;

import io.netty.channel.ChannelFuture;
import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/server/network/ServerConnectionListener", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ServerConnection", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefServerConnection {
    @HandleBy(reference = "Lnet/minecraft/server/network/ServerConnectionListener;channels:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/ServerConnection;listeningChannels:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/ServerConnection;f:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ServerConnection;g:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public final List<ChannelFuture> channels = null;
    @HandleBy(reference = "Lnet/minecraft/server/network/ServerConnectionListener;connections:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/ServerConnection;connectedChannels:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/ServerConnection;g:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_15_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ServerConnection;h:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public final List<RefNetworkManager> connections = null;
}
