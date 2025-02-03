package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Optional;

@HandleBy(reference = "net/minecraft/world/item/component/ResolvableProfile", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefResolvableProfile {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/ResolvableProfile;name()Ljava/util/Optional;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native Optional<String> name();
}
