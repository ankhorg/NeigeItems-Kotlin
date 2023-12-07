package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/NumericTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/NBTNumber", predicates = "craftbukkit_version:[v1_13_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTBase", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefNbtNumber extends RefNbtBase {
  RefNbtNumber() {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsLong()J", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;asLong()J", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTNumber;d()J", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public native long asLong();

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsInt()I", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;asInt()I", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTNumber;e()I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public native int asInt();

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsShort()S", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;asShort()S", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTNumber;f()S", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public native short asShort();

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsByte()B", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;asByte()B", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTNumber;g()B", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public native byte asByte();

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsDouble()D", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;asDouble()D", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTNumber;asDouble()D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public native double asDouble();

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsFloat()F", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;asFloat()F", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTNumber;i()F", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public native float asFloat();

  @HandleBy(reference = "Lnet/minecraft/nbt/NumericTag;getAsNumber()Ljava/lang/Number;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTNumber;j()Ljava/lang/Number;", predicates = "craftbukkit_version:[v1_13_R1,)")
  public native Number asNumber();
}
