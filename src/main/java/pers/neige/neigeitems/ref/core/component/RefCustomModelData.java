package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.List;

@HandleBy(reference = "net/minecraft/world/item/component/CustomModelData", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefCustomModelData {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;<init>(I)V", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public RefCustomModelData(int value) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;<init>(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_21_R4,)")
    public RefCustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;value()I", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native int value();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;floats()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native List<Float> floats();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;flags()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native List<Boolean> flags();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;strings()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native List<String> strings();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/CustomModelData;colors()Ljava/util/List;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native List<Integer> colors();
}
