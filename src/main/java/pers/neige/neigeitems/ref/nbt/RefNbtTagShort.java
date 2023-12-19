package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/ShortTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagShort", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagShort extends RefNbtNumber {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagShort;<init>(S)V", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
  public RefNbtTagShort(short value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/ShortTag;valueOf(S)Lnet/minecraft/nbt/ShortTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagShort;a(S)Lnet/minecraft/server/v1_15_R1/NBTTagShort;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
  public static native RefNbtTagShort of(short value);
}
