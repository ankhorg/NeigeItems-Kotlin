package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/nbt/StringTag", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NBTTagString", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNbtTagString extends RefNbtBase {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagString;<init>(Ljava/lang/String;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
    public RefNbtTagString(String value) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/nbt/StringTag;valueOf(Ljava/lang/String;)Lnet/minecraft/nbt/StringTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/NBTTagString;a(Ljava/lang/String;)Lnet/minecraft/server/v1_15_R1/NBTTagString;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
    public native static RefNbtTagString of(String value);

    @HandleBy(reference = "Lnet/minecraft/nbt/Tag;getAsString()Ljava/lang/String;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/NBTTagString;asString()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NBTTagString;c_()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native String asString0();
}
