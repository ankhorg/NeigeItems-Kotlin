package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/chat/Component")
public interface RefComponent {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")
    static RefMutableComponent literal(String key) {
        return null;
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")
    static RefMutableComponent translatable(String key) {
        return null;
    }
}
