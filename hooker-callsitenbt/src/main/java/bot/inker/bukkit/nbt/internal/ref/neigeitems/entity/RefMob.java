package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.ai.navigation.RefPathNavigation;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefVec3;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityInsentient")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/Mob")
public abstract class RefMob extends RefEntityLiving {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityInsentient;getNavigation()Lnet/minecraft/server/v1_12_R1/NavigationAbstract;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/Mob;getNavigation()Lnet/minecraft/world/entity/ai/navigation/PathNavigation;")
    public native RefPathNavigation getNavigation();
}
