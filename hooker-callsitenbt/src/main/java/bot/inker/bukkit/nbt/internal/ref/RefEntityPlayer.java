package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityPlayer")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/level/ServerPlayer")
public final class RefEntityPlayer extends RefEntityHuman {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;playerInteractManager:Lnet/minecraft/server/v1_12_R1/PlayerInteractManager;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/level/ServerPlayer;gameMode:Lnet/minecraft/server/level/ServerPlayerGameMode;")
    public final RefPlayerInteractManager playerInteractManager = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;playerConnection:Lnet/minecraft/server/v1_12_R1/PlayerConnection;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/level/ServerPlayer;connection:Lnet/minecraft/server/network/ServerGamePacketListenerImpl;")
    public final RefPlayerConnection playerConnection = null;
}
