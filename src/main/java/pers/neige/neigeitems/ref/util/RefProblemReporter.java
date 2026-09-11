package pers.neige.neigeitems.ref.util;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/util/ProblemReporter", isInterface = true, predicates = "craftbukkit_version:[v26_1,)")
public interface RefProblemReporter {
    @HandleBy(reference = "Lnet/minecraft/util/ProblemReporter;DISCARDING:Lnet/minecraft/util/ProblemReporter;", predicates = "craftbukkit_version:[v26_1,)")
    RefProblemReporter DISCARDING = null;
}
