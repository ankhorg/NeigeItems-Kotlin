package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import io.netty.channel.ChannelFuture;

import java.util.List;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/ServerConnection")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/network/ServerConnectionListener")
public final class RefServerConnection {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/ServerConnection;g:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/ServerConnection;f:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/ServerConnection;listeningChannels:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerConnectionListener;channels:Ljava/util/List;", accessor = true)
    public final List<ChannelFuture> channels = null;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/ServerConnection;h:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/ServerConnection;g:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/ServerConnection;connectedChannels:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerConnectionListener;connections:Ljava/util/List;", accessor = true)
    public final List<RefNetworkManager> connections = null;
}
