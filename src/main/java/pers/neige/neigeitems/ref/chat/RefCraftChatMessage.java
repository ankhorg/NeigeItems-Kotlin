package pers.neige.neigeitems.ref.chat;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/util/CraftChatMessage", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftChatMessage {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftChatMessage;fromComponent(Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;Lnet/minecraft/server/v1_12_R1/EnumChatFormat;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,v1_15_R1)")
    public static native String fromComponent(RefComponent component, RefChatFormatting defaultColor);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftChatMessage;fromComponent(Lnet/minecraft/network/chat/Component;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftChatMessage;fromComponent(Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static native String fromComponent(RefComponent component);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftChatMessage;fromJSONComponent(Ljava/lang/String;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native String fromJSONComponent(String jsonMessage);
}
