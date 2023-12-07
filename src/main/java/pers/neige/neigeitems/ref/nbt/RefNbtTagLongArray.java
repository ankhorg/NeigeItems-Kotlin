package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/nbt/LongArrayTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagLongArray", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefNbtTagLongArray extends RefNbtBase {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagLongArray;b:[J", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public long[] longs;

  @HandleBy(reference = "Lnet/minecraft/nbt/LongArrayTag;<init>([J)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagLongArray;<init>([J)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public RefNbtTagLongArray(long[] value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/LongArrayTag;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagLongArray;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public RefNbtTagLongArray(List<Long> value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/LongArrayTag;getAsLongArray()[J", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/NBTTagLongArray;getLongs()[J", predicates = "craftbukkit_version:[v1_14_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagLongArray;d()[J", predicates = "craftbukkit_version:[v1_13_R1,)")
  public native long[] getLongs();
}
