package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@HandleBy(reference = "net/minecraft/world/item/component/ResolvableProfile", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefResolvableProfile {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/ResolvableProfile;name()Ljava/util/Optional;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native Optional<String> name();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ResolvableProfile;resolve()Ljava/util/concurrent/CompletableFuture;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R6)")
    public native CompletableFuture<RefResolvableProfile> resolve();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ResolvableProfile;pollResolve()Lnet/minecraft/world/item/component/ResolvableProfile;", predicates = "craftbukkit_version:[v1_21_R5,v1_21_R6)")
    public native @org.jetbrains.annotations.Nullable RefResolvableProfile pollResolve();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ResolvableProfile;isResolved()Z", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R6)")
    public native boolean isResolved();
}
