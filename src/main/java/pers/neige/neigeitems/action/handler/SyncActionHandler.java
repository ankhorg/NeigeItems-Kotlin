package pers.neige.neigeitems.action.handler;

import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface SyncActionHandler extends BiFunction<ActionContext, String, CompletableFuture<ActionResult>> {
}
