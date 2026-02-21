package pers.neige.neigeitems.ref.spigot;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;

@HandleBy(reference = "org/spigotmc/TrackingRange", predicates = "craftbukkit_version:[v1_21_R1,)")
public class RefTrackingRange {
    @HandleBy(reference = "Lorg/spigotmc/TrackingRange;getEntityTrackingRange(Lnet/minecraft/world/entity/Entity;I)I", predicates = "craftbukkit_version:[v1_21_R1,)")
    public native static int getEntityTrackingRange(RefEntity entity, int defaultRange);
}
