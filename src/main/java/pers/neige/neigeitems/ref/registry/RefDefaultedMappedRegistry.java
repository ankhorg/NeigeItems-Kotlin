package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/DefaultedMappedRegistry", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefDefaultedMappedRegistry<T> extends RefMappedRegistry<T> implements RefDefaultedRegistry<T> {
}
