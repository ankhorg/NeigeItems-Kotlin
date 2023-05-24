package pers.neige.neigeitems.nms;

import javax.annotation.Nullable;
import java.util.*;

public class ItemTagData {
    protected ItemTagType type;

    public ItemTagType getType() {
        return this.type;
    }

    protected Object data;

    public ItemTagData(String data) {
        this.type = ItemTagType.STRING;
        this.data = NMSGeneric.asNBT(data);
    }
    
    public ItemTagData(Byte data) {
        this.type = ItemTagType.BYTE;
        this.data = NMSGeneric.asNBT(data);
    }
    
    public ItemTagData(byte[] data) {
        type = ItemTagType.BYTE_ARRAY;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(int data) {
        this.type = ItemTagType.INT;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(int[] data) {
        this.type = ItemTagType.INT_ARRAY;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(double data) {
        this.type = ItemTagType.DOUBLE;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(float data) {
        this.type = ItemTagType.FLOAT;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(short data) {
        this.type = ItemTagType.SHORT;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(long data) {
        this.type = ItemTagType.LONG;
        this.data = NMSGeneric.asNBT(data);
    }

    public ItemTagData(ItemTag data) {
        this.type = ItemTagType.COMPOUND;
        this.data = data.getNbt();
    }

    public ItemTagData(ItemTagList data) {
        this.type = ItemTagType.LIST;
        this.data = data.getNbt();
    }

    public ItemTagData(Object data) {
        type = NMSGeneric.getNBTType(data);
        this.data = data;
    }

    public ItemTagData(List<?> data) {
        this.type = ItemTagType.LIST;
        this.data = NMSGeneric.asNBT(data);
    }

    public String asString() {
        return NMSGeneric.asString(data);
    }

    public byte asByte() {
        return NMSGeneric.asByte(data);
    }

    public byte[] asByteArray() {
        return NMSGeneric.asByteArray(data);
    }

    public int asInt() {
        return NMSGeneric.asInt(data);
    }

    public int[] asIntArray() {
        return NMSGeneric.asIntArray(data);
    }

    public double asDouble() {
        return NMSGeneric.asDouble(data);
    }

    public float asFloat() {
        return NMSGeneric.asFloat(data);
    }

    public short asShort() {
        return NMSGeneric.asShort(data);
    }

    public long asLong() {
        return NMSGeneric.asLong(data);
    }

    public ItemTag asCompound() {
        return new ItemTag(data);
    }

    @Nullable
    public ItemTagList asList() {
        if (type == ItemTagType.LIST) {
            return new ItemTagList((AbstractList<Object>)data);
        } else {
            ItemTagList temp = new ItemTagList();
            temp.getNbt().add(data);
            return temp;
        }
    }

    public int hashCode() {
        return Objects.hash(type, data);
    }

    public String toString() {
        return NMSGeneric.toString(data);
    }
}