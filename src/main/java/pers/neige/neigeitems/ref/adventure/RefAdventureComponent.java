package pers.neige.neigeitems.ref.adventure;

import net.kyori.adventure.text.Component;
import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;

@HandleBy(reference = "io/papermc/paper/adventure/AdventureComponent", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefAdventureComponent {
    @HandleBy(reference = "Lio/papermc/paper/adventure/AdventureComponent;<init>(Lnet/kyori/adventure/text/Component;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefAdventureComponent(Component adventure) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lio/papermc/paper/adventure/AdventureComponent;deepConverted()Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefComponent deepConverted();
}
