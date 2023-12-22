package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

public final class NbtType {
  public static final byte TAG_END = 0;

  public static final byte TAG_BYTE = 1;

  public static final byte TAG_SHORT = 2;

  public static final byte TAG_INT = 3;

  public static final byte TAG_LONG = 4;

  public static final byte TAG_FLOAT = 5;

  public static final byte TAG_DOUBLE = 6;

  public static final byte TAG_BYTE_ARRAY = 7;

  public static final byte TAG_STRING = 8;

  public static final byte TAG_LIST = 9;

  public static final byte TAG_COMPOUND = 10;

  public static final byte TAG_INT_ARRAY = 11;

  public static final byte TAG_LONG_ARRAY = 12;

  public static final byte TAG_ANY_NUMBER = 99;

  private NbtType() {
    throw new UnsupportedOperationException();
  }
}
