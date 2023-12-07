package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/FloatTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagFloat", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefNbtTagFloat extends RefNbtNumber {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagFloat;<init>(F)V", predicates = "craftbukkit_version:[v1_12_R1,v1_16_R1)")
  public RefNbtTagFloat(float value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/FloatTag;valueOf(F)Lnet/minecraft/nbt/FloatTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagFloat;a(F)Lnet/minecraft/server/v1_15_R1/NBTTagFloat;", predicates = "craftbukkit_version:[v1_15_R1,)")
  public static native RefNbtTagFloat of(float value);
}
