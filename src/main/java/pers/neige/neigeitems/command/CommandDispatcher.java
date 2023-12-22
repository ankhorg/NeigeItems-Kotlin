package pers.neige.neigeitems.command;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ServerUtils;
import pers.neige.neigeitems.utils.CommandUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandDispatcher {
    @NotNull
    private final Map<String, CommandDispatcher> post = new HashMap<>();
    @NotNull
    private final String key;
    @Nullable
    private Consumer<CommandContext> executor;
    @Nullable
    private Function<CommandContext, List<String>> suggester;
    private int index = -1;
    @Nullable
    private CommandDispatcher pre = null;
    @Nullable
    private CommandDispatcher onlyPost = null;
    private PluginCommand command;
    private boolean only = false;

    public CommandDispatcher(
            @NotNull String key
    ) {
        this.key = key;
    }

    public CommandDispatcher(
            @NotNull String key,
            boolean only
    ) {
        this.key = key;
        this.only = only;
    }

    public static CommandDispatcher command(
            @NotNull String key
    ) {
        return new CommandDispatcher(key);
    }

    public static CommandDispatcher command(
            @NotNull String key,
            boolean only
    ) {
        return new CommandDispatcher(key, only);
    }


    protected void addIndex() {
        this.index++;
    }

    public boolean execute(CommandContext context) {
        String[] args = context.getArgs();
        int nextIndex = index + 1;
        // 参数下探
        if (args.length > nextIndex) {
            String arg = args[nextIndex];
            CommandDispatcher postDispatcher = post.get(arg);
            if (postDispatcher != null) {
                return postDispatcher.execute(context.push(key));
            } else if (onlyPost != null && !Objects.equals(arg, "")) {
                return onlyPost.execute(context.push(key));
                // 参数合并
            } else {
                merge(context, args);
                if (executor != null) {
                    executor.accept(context);
                    return true;
                }
            }
            // 尾级处理
        } else if (args.length == nextIndex) {
            if (executor != null) {
                executor.accept(context);
                return true;
            }
        }
        return true;
    }

    public List<String> suggest(CommandContext context) {
        String[] args = context.getArgs();
        int nextIndex = index + 1;
        // 参数下探
        if (args.length > nextIndex) {
            String arg = args[nextIndex];
            CommandDispatcher postDispatcher = post.get(arg);
            if (postDispatcher != null) {
                return postDispatcher.suggest(context.push(key));
            } else if (onlyPost != null && !Objects.equals(arg, "")) {
                return onlyPost.suggest(context.push(key));
                // 参数合并
            } else {
                merge(context, args);
                if (suggester != null) {
                    List<String> result = suggester.apply(context);
                    return result == null ? new ArrayList<>() : result;
                } else {
                    return new ArrayList<>(post.keySet());
                }
            }
            // 尾级处理
        } else if (args.length == nextIndex) {
            if (suggester != null) {
                List<String> result = suggester.apply(context);
                return result == null ? new ArrayList<>() : result;
            } else {
                new ArrayList<>(post.keySet());
            }
        }
        return new ArrayList<>();
    }

    private void merge(CommandContext context, String[] args) {
        List<String> newArgs;
        if (index == -1) {
            newArgs = new ArrayList<>();
        } else {
            newArgs = new ArrayList<>(Arrays.asList(args).subList(0, index));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            if (i != -1) {
                sb.append(args[i]);
                if (i != (args.length - 1)) {
                    sb.append(" ");
                }
            }
        }
        newArgs.add(sb.toString());
        context.setArgs(newArgs.toArray(ArrayUtils.EMPTY_STRING_ARRAY));
        if (index != -1) {
            context.setArg(newArgs.get(index));
        } else {
            context.setArg(null);
        }
    }


    @NotNull
    public String getKey() {
        return key;
    }

    public void setPre(
            @Nullable CommandDispatcher pre
    ) {
        this.pre = pre;
    }

    public boolean isOnly() {
        return only;
    }

    public CommandDispatcher setOnly(boolean only) {
        this.only = only;
        return this;
    }

    @NotNull
    public CommandDispatcher then(CommandDispatcher dispatcher) {
        dispatcher.addIndex();
        dispatcher.setPre(this);
        post.put(dispatcher.getKey(), dispatcher);
        if (dispatcher.isOnly()) {
            this.onlyPost = dispatcher;
        }
        return this;
    }

//    @NotNull
//    public CommandDispatcher execute(Function<CommandContext, Boolean> executor) {
//        this.executor = executor;
//        return this;
//    }

    @NotNull
    public CommandDispatcher execute(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    @NotNull
    public CommandDispatcher suggest(Function<CommandContext, List<String>> suggester) {
        this.suggester = suggester;
        return this;
    }

    public void register() {
        register(NeigeItems.INSTANCE.getPlugin(), null, null);
    }

    public void register(
            Plugin plugin
    ) {
        register(plugin, null, null);
    }

    public void register(
            @Nullable String permission
    ) {
        register(NeigeItems.INSTANCE.getPlugin(), permission, null);
    }

    public void register(
            @Nullable List<String> aliases
    ) {
        register(NeigeItems.INSTANCE.getPlugin(), null, aliases);
    }

    public void register(
            Plugin plugin,
            @Nullable String permission
    ) {
        register(plugin, permission, null);
    }

    public void register(
            Plugin plugin,
            @Nullable List<String> aliases
    ) {
        register(plugin, null, aliases);
    }

    public void register(
            @Nullable String permission,
            @Nullable List<String> aliases
    ) {
        register(NeigeItems.INSTANCE.getPlugin(), permission, aliases);
    }

    public void register(
            Plugin plugin,
            @Nullable String permission,
            @Nullable List<String> aliases
    ) {
        if (index == -1) {
            unregister();
            command = CommandUtils.newPluginCommand(key, plugin);
            if (command != null) {
                if (aliases != null) {
                    command.setAliases(aliases);
                }
                if (permission == null) {
                    command.setPermission("*");
                } else {
                    command.setPermission(permission);
                }
                command.setExecutor((sender, command, label, args) -> execute(new CommandContext(sender, args)));
                command.setTabCompleter((sender, command, label, args) -> suggest(new CommandContext(sender, args)));
                CommandUtils.getCommandMap().register(key, command);
            }
        }
    }

    public void unregister() {
        if (command != null) {
            CommandUtils.unregisterCommand(key);
            CommandUtils.unregisterCommand(key + ":" + key);
            for (String alias : command.getAliases()) {
                CommandUtils.unregisterCommand(alias);
                CommandUtils.unregisterCommand(key + ":" + alias);
            }
            ServerUtils.syncCommands();
        }
    }
}
