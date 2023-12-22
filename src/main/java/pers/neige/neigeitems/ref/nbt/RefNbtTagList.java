package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/ListTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagList", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagList extends RefNbtBase {
  @HandleBy(reference = "Lnet/minecraft/nbt/ListTag;<init>()V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagList;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  public RefNbtTagList() {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Ljava/util/List;get(I)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/NBTTagList;get(I)Lnet/minecraft/server/v1_14_R1/NBTBase;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagList;c(I)Lnet/minecraft/server/v1_13_R1/NBTBase;", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagList;i(I)Lnet/minecraft/server/v1_12_R1/NBTBase;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
  public native RefNbtBase get(int index);

  @HandleBy(reference = "Lnet/minecraft/nbt/ListTag;size()I", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagList;size()I", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  public native int size();

  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagList;a(ILnet/minecraft/server/v1_12_R1/NBTBase;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
  public native void set0(int index, RefNbtBase element);

  @HandleBy(reference = "Lnet/minecraft/nbt/CollectionTag;set(ILnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagList;set(ILnet/minecraft/server/v1_13_R1/NBTBase;)Lnet/minecraft/server/v1_13_R1/NBTBase;", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
  public native RefNbtBase set1(int index, RefNbtBase element);

  @HandleBy(reference = "Lnet/minecraft/nbt/ListTag;setTag(ILnet/minecraft/nbt/Tag;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
  public native boolean set2(int index, RefNbtBase element);

  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagList;add(Lnet/minecraft/server/v1_12_R1/NBTBase;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
  public native void add0(RefNbtBase element);

  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagList;add(Lnet/minecraft/server/v1_13_R1/NBTBase;)Z", predicates = "craftbukkit_version:[v1_13_R1,v1_14_R1)")
  public native boolean add1(RefNbtBase element);

  @HandleBy(reference = "Ljava/util/List;add(ILjava/lang/Object;)V", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/NBTTagList;add(ILnet/minecraft/server/v1_14_R1/NBTBase;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
  public native void add2(int index, RefNbtBase element);

  @HandleBy(reference = "Ljava/util/List;remove(I)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagList;remove(I)Lnet/minecraft/server/v1_12_R1/NBTBase;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  public native RefNbtBase remove(int index);
}
