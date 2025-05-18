package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;

import java.util.List;

@HandleBy(reference = "net/minecraft/world/item/component/ItemLore", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefItemLore {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;<init>(Ljava/util/List;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public RefItemLore(List<RefComponent> lines, List<RefComponent> styledLines) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;lines()Ljava/util/List;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native List<RefComponent> lines();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;styledLines()Ljava/util/List;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native List<RefComponent> styledLines();
}
