package org.inksnow.ankhinvoke.example.ref.craftbukkit;

import org.bukkit.entity.Entity;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefCraftEntity implements Entity {
}
