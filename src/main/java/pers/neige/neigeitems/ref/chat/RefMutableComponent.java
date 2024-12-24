package pers.neige.neigeitems.ref.chat;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/chat/MutableComponent", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_16_R1/IChatMutableComponent", predicates = "craftbukkit_version:[v1_16_R1,v1_17_R1)")
public final class RefMutableComponent extends RefComponent {
    @HandleBy(reference = "Lnet/minecraft/network/chat/MutableComponent;append(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefMutableComponent append(String text);

    @HandleBy(reference = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefMutableComponent append(RefComponent text);

    @HandleBy(reference = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefMutableComponent withStyle(RefChatFormatting formatting);
}
