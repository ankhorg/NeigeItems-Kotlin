package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/nbt/IntArrayTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagIntArray", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagIntArray extends RefNbtBase {
  @HandleBy(reference = "Lnet/minecraft/nbt/IntArrayTag;<init>([I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagIntArray;<init>([I)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  public RefNbtTagIntArray(int[] value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/IntArrayTag;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagIntArray;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  public RefNbtTagIntArray(List<Integer> value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/IntArrayTag;getAsIntArray()[I", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/NBTTagIntArray;getInts()[I", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagIntArray;d()[I", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  public native int[] getInts();
}
