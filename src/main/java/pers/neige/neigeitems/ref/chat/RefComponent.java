package pers.neige.neigeitems.ref.chat;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/chat/Component", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/IChatBaseComponent", isInterface = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefComponent {
    @HandleBy(reference = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_19_R1,)")
    public static RefMutableComponent literal(String key) {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_19_R1,)")
    public static RefMutableComponent translatable(String key) {
        return null;
    }

    @HandleBy(reference = "Lnet/minecraft/network/chat/Component;getString()Ljava/lang/String;", isInterface = true, predicates = "craftbukkit_version:[v1_18_R1,)")
    public native String getString();
}
