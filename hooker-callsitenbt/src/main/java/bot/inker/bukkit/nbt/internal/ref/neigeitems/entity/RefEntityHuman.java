package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityHuman")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/player/Player")
public abstract class RefEntityHuman extends RefEntityLiving {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;getAbsorptionHearts()F")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/player/Player;getAbsorptionAmount()F")
    public native float getAbsorptionAmount();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;setAbsorptionHearts(F)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V")
    public native void setAbsorptionAmount(float amount);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;attack(Lnet/minecraft/server/v1_12_R1/Entity;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/player/Player;attack(Lnet/minecraft/world/entity/Entity;)V")
    public native void attack(RefEntity target);
}
