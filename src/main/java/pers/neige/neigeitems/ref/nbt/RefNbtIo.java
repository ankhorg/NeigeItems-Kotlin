package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.io.*;

@HandleBy(reference = "net/minecraft/nbt/NbtIo", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTCompressedStreamTools", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefNbtIo {
    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;readCompressed(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R2/NBTCompressedStreamTools;a(Ljava/io/File;)Lnet/minecraft/server/v1_16_R2/NBTTagCompound;", predicates = "craftbukkit_version:[v1_16_R2,v1_17_R1)")
    public native static RefNbtTagCompound readCompressed(File file) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;readCompressed(Ljava/io/InputStream;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTCompressedStreamTools;a(Ljava/io/InputStream;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native static RefNbtTagCompound readCompressed(InputStream stream) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/io/File;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R2/NBTCompressedStreamTools;a(Lnet/minecraft/server/v1_16_R2/NBTTagCompound;Ljava/io/File;)V", predicates = "craftbukkit_version:[v1_16_R2,v1_17_R1)")
    public native static void writeCompressed(RefNbtTagCompound compound, File file) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/io/OutputStream;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTCompressedStreamTools;a(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;Ljava/io/OutputStream;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native static void writeCompressed(RefNbtTagCompound compound, OutputStream stream) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;read(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native static RefNbtTagCompound read(File file) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;read(Ljava/io/DataInput;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R2/NBTCompressedStreamTools;a(Ljava/io/DataInput;)Lnet/minecraft/server/v1_16_R2/NBTTagCompound;", predicates = "craftbukkit_version:[v1_16_R2,v1_17_R1)")
    public native static RefNbtTagCompound read(DataInput input) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTCompressedStreamTools;a(Ljava/io/DataInputStream;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,v1_16_R2)")
    public native static RefNbtTagCompound read(DataInputStream stream) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;write(Lnet/minecraft/nbt/CompoundTag;Ljava/io/File;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native static void write(RefNbtTagCompound nbt, File file) throws IOException;

    @HandleBy(reference = "Lnet/minecraft/nbt/NbtIo;write(Lnet/minecraft/nbt/CompoundTag;Ljava/io/DataOutput;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTCompressedStreamTools;a(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;Ljava/io/DataOutput;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native static void write(RefNbtTagCompound nbt, DataOutput output) throws IOException;
}
