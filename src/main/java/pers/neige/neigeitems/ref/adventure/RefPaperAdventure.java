package pers.neige.neigeitems.ref.adventure;

import net.kyori.adventure.text.Component;
import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.ref.chat.RefComponent;

import java.util.ArrayList;
import java.util.List;

@HandleBy(reference = "io/papermc/paper/adventure/PaperAdventure", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefPaperAdventure {
    @HandleBy(reference = "Lio/papermc/paper/adventure/PaperAdventure;asAdventure(Lnet/minecraft/network/chat/Component;)Lnet/kyori/adventure/text/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native @NotNull Component asAdventure(@Nullable RefComponent component);

    @HandleBy(reference = "Lio/papermc/paper/adventure/PaperAdventure;asAdventure(Ljava/util/List;)Ljava/util/ArrayList;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native ArrayList<Component> asAdventure(List<? extends RefComponent> vanillas);

    @Contract("null -> null; !null -> !null")
    @HandleBy(reference = "Lio/papermc/paper/adventure/PaperAdventure;asVanilla(Lnet/kyori/adventure/text/Component;)Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native RefComponent asVanilla(@Nullable Component component);

    @HandleBy(reference = "Lio/papermc/paper/adventure/PaperAdventure;asVanilla(Ljava/util/List;)Ljava/util/List;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static native List<RefComponent> asVanilla(List<? extends Component> adventures);
}
