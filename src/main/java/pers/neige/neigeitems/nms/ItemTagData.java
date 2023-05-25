package pers.neige.neigeitems.nms;

import pers.neige.neigeitems.utils.NMSUtils;

import javax.annotation.Nullable;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

public class ItemTagData {
    protected ItemTagType type;

    public ItemTagType getType() {
        return this.type;
    }

    protected Object data;

    public ItemTagData(String data) {
        this.type = ItemTagType.STRING;
        this.data = NMSUtils.asNBT(data);
    }
    
    public ItemTagData(Byte data) {
        this.type = ItemTagType.BYTE;
        this.data = NMSUtils.asNBT(data);
    }
    
    public ItemTagData(byte[] data) {
        type = ItemTagType.BYTE_ARRAY;
        this.data = NMSUtils.asNBT(data);
    }

    public ItemTagData(int data) {
        this.type = ItemTagType.INT;
        this.data = NMSUtils.asNBT(data);
    }

    public ItemTagData(int[] data) {
        this.type = ItemTagType.INT_ARRAY;
        this.data = NMSUtils.asNBT(data);
    }

    public ItemTagData(double data) {
        this.type = ItemTagType.DOUBLE;
        this.data = NMSUtils.asNBT(data);
    }

    public ItemTagData(float data) {
        this.type = ItemTagType.FLOAT;
        this.data = NMSUtils.asNBT(data);
    }

    public ItemTagData(short data) {
        this.type = ItemTagType.SHORT;
        this.data = NMSUtils.asNBT(data);
    }

    public ItemTagData(long data) {
        this.type = ItemTagType.LONG;
        this.data = NMSUtils.asNBT(data);
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
        type = NMSUtils.getNBTType(data);
        this.data = data;
    }

    public ItemTagData(List<?> data) {
        this.type = ItemTagType.LIST;
        this.data = NMSUtils.asNBT(data);
    }

    public String asString() {
        return NMSUtils.asString(data);
    }

    public byte asByte() {
        return NMSUtils.asByte(data);
    }

    public byte[] asByteArray() {
        return NMSUtils.asByteArray(data);
    }

    public int asInt() {
        return NMSUtils.asInt(data);
    }

    public int[] asIntArray() {
        return NMSUtils.asIntArray(data);
    }

    public double asDouble() {
        return NMSUtils.asDouble(data);
    }

    public float asFloat() {
        return NMSUtils.asFloat(data);
    }

    public short asShort() {
        return NMSUtils.asShort(data);
    }

    public long asLong() {
        return NMSUtils.asLong(data);
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
        return NMSUtils.toString(data);
    }
}