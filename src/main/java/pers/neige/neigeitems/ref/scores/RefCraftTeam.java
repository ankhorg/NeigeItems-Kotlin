package pers.neige.neigeitems.ref.scores;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "java/lang/Object", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefCraftTeam {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/scoreboard/CraftTeam;team:Lnet/minecraft/world/scores/PlayerTeam;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefPlayerTeam team;
}
