package pers.neige.neigeitems.ref.chat;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/chat/Component", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/IChatBaseComponent", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public interface RefComponent {
    @HandleBy(reference = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    static RefMutableComponent literal(String key) {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    static RefMutableComponent translatable(String key) {
        return null;
    }
}
