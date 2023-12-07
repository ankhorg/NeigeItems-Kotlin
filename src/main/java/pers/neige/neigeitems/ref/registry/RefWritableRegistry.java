package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/WritableRegistry", predicates = "craftbukkit_version:[v1_17_R1,)")
public interface RefWritableRegistry<T> extends RefRegistry<T> {
}
