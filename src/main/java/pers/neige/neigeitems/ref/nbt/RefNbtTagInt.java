package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/IntTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagInt", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagInt extends RefNbtNumber {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagInt;<init>(I)V", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
  public RefNbtTagInt(int value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/IntTag;valueOf(I)Lnet/minecraft/nbt/IntTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagInt;a(I)Lnet/minecraft/server/v1_15_R1/NBTTagInt;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
  public static native RefNbtTagInt of(int value);
}
