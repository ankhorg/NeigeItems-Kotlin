package bot.inker.bukkit.nbt.internal.ref.neigeitems.chat;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_16_R1, reference = "net/minecraft/server/v1_16_R1/IChatMutableComponent")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/chat/MutableComponent")
public final class RefMutableComponent implements RefComponent {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/chat/MutableComponent;append(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")
    public native RefMutableComponent append(String text);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;")
    public native RefMutableComponent append(RefComponent text);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;")
    public native RefMutableComponent withStyle(RefChatFormatting formatting);
}
