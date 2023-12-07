package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@HandleBy(reference = "net/minecraft/nbt/CompoundTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagCompound", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefNbtTagCompound extends RefNbtBase {
  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;<init>()V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public RefNbtTagCompound() {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;tags:Ljava/util/Map;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;map:Ljava/util/Map;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
  public Map<String, RefNbtBase> tags;

  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;set(Ljava/lang/String;Lnet/minecraft/server/v1_12_R1/NBTBase;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
  public native void set0(String key, RefNbtBase value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/NBTTagCompound;set(Ljava/lang/String;Lnet/minecraft/server/v1_14_R1/NBTBase;)Lnet/minecraft/server/v1_14_R1/NBTBase;", predicates = "craftbukkit_version:[v1_14_R1,)")
  public native RefNbtBase set1(String key, RefNbtBase value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setByte(Ljava/lang/String;B)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setByte(String key, byte value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putShort(Ljava/lang/String;S)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setShort(Ljava/lang/String;S)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setShort(String key, short value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setInt(Ljava/lang/String;I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setInt(String key, int value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putLong(Ljava/lang/String;J)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setLong(Ljava/lang/String;J)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setLong(String key, long value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putUUID(Ljava/lang/String;Ljava/util/UUID;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;a(Ljava/lang/String;Ljava/util/UUID;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setUUID(String key, UUID uuid);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putFloat(Ljava/lang/String;F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setFloat(Ljava/lang/String;F)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setFloat(String key, float value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putDouble(Ljava/lang/String;D)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setDouble(Ljava/lang/String;D)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setDouble(String key, double value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putString(Ljava/lang/String;Ljava/lang/String;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setString(Ljava/lang/String;Ljava/lang/String;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setString(String key, String value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putByteArray(Ljava/lang/String;[B)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setByteArray(Ljava/lang/String;[B)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setByteArray(String key, byte[] value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putByteArray(Ljava/lang/String;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  public native void setByteArray(String key, List<Byte> value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putIntArray(Ljava/lang/String;[I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setIntArray(Ljava/lang/String;[I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setIntArray(String key, int[] value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putIntArray(Ljava/lang/String;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagCompound;b(Ljava/lang/String;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_13_R1,)")
  public native void setIntArray(String key, List<Integer> value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putLongArray(Ljava/lang/String;[J)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagCompound;a(Ljava/lang/String;[J)V", predicates = "craftbukkit_version:[v1_13_R1,)")
  public native void setLongArray(String key, long[] value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putLongArray(Ljava/lang/String;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagCompound;c(Ljava/lang/String;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_13_R1,)")
  public native void setLongArray(String key, List<Long> value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;putBoolean(Ljava/lang/String;Z)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;setBoolean(Ljava/lang/String;Z)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setBoolean(String key, boolean value);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;get(Ljava/lang/String;)Lnet/minecraft/nbt/Tag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;get(Ljava/lang/String;)Lnet/minecraft/server/v1_12_R1/NBTBase;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native RefNbtBase get(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getTagType(Ljava/lang/String;)B", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/NBTTagCompound;d(Ljava/lang/String;)B", predicates = "craftbukkit_version:[v1_16_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;e(Ljava/lang/String;)B", predicates = "craftbukkit_version:[v1_15_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;d(Ljava/lang/String;)B", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native byte getType(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;hasKey(Ljava/lang/String;)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native boolean hasKey(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;hasKeyOfType(Ljava/lang/String;I)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native boolean hasKeyOfType(String key, int type);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getByte(Ljava/lang/String;)B", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getByte(Ljava/lang/String;)B", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native byte getByte(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getShort(Ljava/lang/String;)S", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getShort(Ljava/lang/String;)S", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native short getShort(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getInt(Ljava/lang/String;)I", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getInt(Ljava/lang/String;)I", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native int getInt(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getLong(Ljava/lang/String;)J", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getLong(Ljava/lang/String;)J", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native long getLong(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getUUID(Ljava/lang/String;)Ljava/util/UUID;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;a(Ljava/lang/String;)Ljava/util/UUID;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native UUID getUUID(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getFloat(Ljava/lang/String;)F", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getFloat(Ljava/lang/String;)F", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native float getFloat(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getDouble(Ljava/lang/String;)D", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getDouble(Ljava/lang/String;)D", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native double getDouble(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getString(Ljava/lang/String;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getString(Ljava/lang/String;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native String getString(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getByteArray(Ljava/lang/String;)[B", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getByteArray(Ljava/lang/String;)[B", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native byte[] getByteArray(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getIntArray(Ljava/lang/String;)[I", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getIntArray(Ljava/lang/String;)[I", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native int[] getIntArray(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getLongArray(Ljava/lang/String;)[J", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/NBTTagCompound;getLongArray(Ljava/lang/String;)[J", predicates = "craftbukkit_version:[v1_14_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagCompound;o(Ljava/lang/String;)[J", predicates = "craftbukkit_version:[v1_13_R1,)")
  public native long[] getLongArray(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getCompound(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getCompound(Ljava/lang/String;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native RefNbtTagCompound getCompound(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getList(Ljava/lang/String;I)Lnet/minecraft/server/v1_12_R1/NBTTagList;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native RefNbtTagList getList(String key, int var1);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getBoolean(Ljava/lang/String;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;getBoolean(Ljava/lang/String;)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native boolean getBoolean(String key);

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;getAllKeys()Ljava/util/Set;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagCompound;getKeys()Ljava/util/Set;", predicates = "craftbukkit_version:[v1_13_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;c()Ljava/util/Set;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native Set<String> getKeys();

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;size()I", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagCompound;e()I", predicates = "craftbukkit_version:[v1_15_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;d()I", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native int size();

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;isEmpty()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;isEmpty()Z", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native boolean isEmpty();

  @HandleBy(reference = "Lnet/minecraft/nbt/CompoundTag;remove(Ljava/lang/String;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;remove(Ljava/lang/String;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void remove(String key);
}
