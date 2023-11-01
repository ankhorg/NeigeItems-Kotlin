package bot.inker.bukkit.nbt.internal.ref.neigeitems.chat;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

import javax.annotation.Nullable;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/IChatBaseComponent$ChatSerializer")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/chat/Component$Serializer")
public final class RefChatSerializer {
    @Nullable
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/IChatBaseComponent$ChatSerializer;a(Ljava/lang/String;)Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/IChatBaseComponent$ChatSerializer;a(Ljava/lang/String;)Lnet/minecraft/server/v1_16_R1/IChatMutableComponent;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/chat/Component$Serializer;fromJson(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")
    public static native RefComponent fromJson(String jsonMessage);
}
