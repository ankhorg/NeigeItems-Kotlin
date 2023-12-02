package bot.inker.bukkit.nbt.internal.ref.neigeitems.server.players;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import com.mojang.authlib.GameProfile;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PlayerList")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/players/PlayerList")
public abstract class RefPlayerList {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z")
    public native boolean isOp(GameProfile gameprofile);
}
