package pers.neige.neigeitems.ref.item.potion;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PotionUtil", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public final class RefPotionRegistry {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PotionRegistry;b(Ljava/lang/String;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native String getTranslationKey(String prefix);
}
