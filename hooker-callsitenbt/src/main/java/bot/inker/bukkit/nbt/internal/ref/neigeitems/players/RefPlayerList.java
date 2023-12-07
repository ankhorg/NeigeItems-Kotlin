package bot.inker.bukkit.nbt.internal.ref.neigeitems.players;

import com.mojang.authlib.GameProfile;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/players/PlayerList", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PlayerList", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefPlayerList {
    @HandleBy(reference = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean isOp(GameProfile gameprofile);
}
