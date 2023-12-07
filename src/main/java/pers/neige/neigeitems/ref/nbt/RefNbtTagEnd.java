package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/EndTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagEnd", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefNbtTagEnd extends RefNbtBase {
  @HandleBy(reference = "Lnet/minecraft/nbt/EndTag;INSTANCE:Lnet/minecraft/nbt/EndTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_16_R3/NBTTagEnd;b:Lnet/minecraft/server/v1_16_R3/NBTTagEnd;", predicates = "craftbukkit_version:[v1_15_R1,)")
  public static final RefNbtTagEnd INSTANCE = null;

  private RefNbtTagEnd() {
    throw new UnsupportedOperationException();
  }
}
