package pers.neige.neigeitems.ref.core;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.registry.RefHolderLookup$Provider;

@HandleBy(reference = "net/minecraft/core/RegistryAccess", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefRegistryAccess extends RefHolderLookup$Provider {
}
