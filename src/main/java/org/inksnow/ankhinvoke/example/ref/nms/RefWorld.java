package org.inksnow.ankhinvoke.example.ref.nms;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/level/Level", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/World", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefWorld {
}
