package bot.inker.bukkit.nbt.internal.ref.neigeitems.chat;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/util/CraftChatMessage")
public final class RefCraftChatMessage {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftChatMessage;fromComponent(Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;Lnet/minecraft/server/v1_12_R1/EnumChatFormat;)Ljava/lang/String;")
    @HandleBy(version = CbVersion.v1_15_R1, reference = "")
    public static native String fromComponent(RefComponent component, RefChatFormatting defaultColor);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/util/CraftChatMessage;fromComponent(Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;)Ljava/lang/String;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftChatMessage;fromComponent(Lnet/minecraft/network/chat/Component;)Ljava/lang/String;")
    public static native String fromComponent(RefComponent component);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/util/CraftChatMessage;fromJSONComponent(Ljava/lang/String;)Ljava/lang/String;")
    public static native String fromJSONComponent(String jsonMessage);
}
