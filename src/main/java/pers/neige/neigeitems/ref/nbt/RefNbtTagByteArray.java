package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/nbt/ByteArrayTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagByteArray", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefNbtTagByteArray extends RefNbtBase {
  @HandleBy(reference = "Lnet/minecraft/nbt/ByteArrayTag;<init>([B)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagByteArray;<init>([B)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public RefNbtTagByteArray(byte[] value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/ByteArrayTag;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagByteArray;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public RefNbtTagByteArray(List<Byte> value) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/ByteArrayTag;getAsByteArray()[B", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_16_R3/NBTTagByteArray;getBytes()[B", predicates = "craftbukkit_version:[v1_14_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagByteArray;c()[B", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native byte[] getBytes();
}
