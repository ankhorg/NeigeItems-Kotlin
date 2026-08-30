package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@HandleBy(reference = "net/minecraft/world/item/component/CustomModelData", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefCustomModelData {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;DEFAULT:Lnet/minecraft/world/item/component/CustomModelData;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R3)")
    public static final RefCustomModelData DEFAULT = null;

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;EMPTY:Lnet/minecraft/world/item/component/CustomModelData;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public static final RefCustomModelData EMPTY = null;

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;<init>(I)V", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R3)")
    public RefCustomModelData(int value) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;<init>(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_21_R3,)")
    public RefCustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;value()I", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R3)")
    public native int value();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;floats()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native List<Float> floats();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;flags()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native List<Boolean> flags();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;strings()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native List<String> strings();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;colors()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native List<Integer> colors();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;getFloat(I)Ljava/lang/Float;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native @Nullable Float getFloat(int index);

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;getBoolean(I)Ljava/lang/Boolean;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native @Nullable Boolean getBoolean(int index);

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;getString(I)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native @Nullable String getString(int index);

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;getColor(I)Ljava/lang/Integer;", predicates = "craftbukkit_version:[v1_21_R3,)")
    public native @Nullable Integer getColor(int index);
}
