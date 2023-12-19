package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/DoubleTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagDouble", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagDouble extends RefNbtNumber {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagDouble;<init>(D)V", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
  public RefNbtTagDouble(double value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/DoubleTag;valueOf(D)Lnet/minecraft/nbt/DoubleTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagDouble;a(D)Lnet/minecraft/server/v1_15_R1/NBTTagDouble;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
  public static native RefNbtTagDouble of(double value);
}
