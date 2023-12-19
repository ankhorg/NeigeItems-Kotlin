package pers.neige.neigeitems.ref.chat;

import org.inksnow.ankhinvoke.comments.HandleBy;

import javax.annotation.Nullable;

@HandleBy(reference = "net/minecraft/network/chat/Component$Serializer", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/IChatBaseComponent$ChatSerializer", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefChatSerializer {
    @Nullable
    @HandleBy(reference = "Lnet/minecraft/network/chat/Component$Serializer;fromJson(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/IChatBaseComponent$ChatSerializer;a(Ljava/lang/String;)Lnet/minecraft/server/v1_16_R1/IChatMutableComponent;", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/IChatBaseComponent$ChatSerializer;a(Ljava/lang/String;)Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;", predicates = "craftbukkit_version:[v1_12_R1,v1_16_R1)")
    public static native RefComponent fromJson(String jsonMessage);
}
