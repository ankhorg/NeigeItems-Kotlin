package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/Tag", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/NBTBase", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTBase", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public abstract class RefNbtBase {
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTBase;createTag(B)Lnet/minecraft/server/v1_13_R1/NBTBase;", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_16_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTBase;createTag(B)Lnet/minecraft/server/v1_12_R1/NBTBase;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native RefNbtBase createTag(byte typeId);

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;getId()B", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTBase;getTypeId()B", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTBase;getTypeId()B", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native byte getTypeId();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;copy()Lnet/minecraft/nbt/Tag;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTBase;clone()Lnet/minecraft/server/v1_13_R1/NBTBase;", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTBase;clone()Lnet/minecraft/server/v1_12_R1/NBTBase;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native RefNbtBase rClone();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;getAsString()Ljava/lang/String;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTBase;asString()Ljava/lang/String;", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTBase;c_()Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native String asString();
}
