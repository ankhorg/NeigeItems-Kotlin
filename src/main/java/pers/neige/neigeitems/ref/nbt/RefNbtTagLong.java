package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/LongTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagLong", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefNbtTagLong extends RefNbtNumber {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagLong;<init>(J)V", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
  public RefNbtTagLong(long value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/LongTag;valueOf(J)Lnet/minecraft/nbt/LongTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagLong;a(J)Lnet/minecraft/server/v1_15_R1/NBTTagLong;", predicates = "craftbukkit_version:[v1_15_R1,)")
  public static native RefNbtTagLong of(long value);
}
