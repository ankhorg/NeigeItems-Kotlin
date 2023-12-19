package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/ByteTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagByte", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagByte extends RefNbtNumber {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagByte;<init>(B)V", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
  public RefNbtTagByte(byte value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/ByteTag;valueOf(B)Lnet/minecraft/nbt/ByteTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagByte;a(B)Lnet/minecraft/server/v1_15_R1/NBTTagByte;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
  public static native RefNbtTagByte of(byte value);

  @HandleBy(reference = "Lnet/minecraft/nbt/ByteTag;valueOf(Z)Lnet/minecraft/nbt/ByteTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagByte;a(Z)Lnet/minecraft/server/v1_15_R1/NBTTagByte;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
  public static native RefNbtTagByte of(boolean value);
}
