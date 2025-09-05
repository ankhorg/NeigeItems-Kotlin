package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Optional;

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

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;getAsString()Ljava/lang/String;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,v1_21_R4)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTBase;asString()Ljava/lang/String;", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTBase;c_()Ljava/lang/String;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native String asString0();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asString()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<String> asString1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asNumber()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Number> asNumber1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asByte()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Byte> asByte1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asShort()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Short> asShort1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asInt()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Integer> asInt1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asLong()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Long> asLong1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asFloat()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Float> asFloat1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asDouble()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Double> asDouble1();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asBoolean()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<Boolean> asBoolean();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asByteArray()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<byte[]> asByteArray();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asIntArray()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<int[]> asIntArray();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asLongArray()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<long[]> asLongArray();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asCompound()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<RefNbtTagCompound> asCompound();

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;asList()Ljava/util/Optional;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R4,)")
    public native Optional<RefNbtTagList> asList();
}
