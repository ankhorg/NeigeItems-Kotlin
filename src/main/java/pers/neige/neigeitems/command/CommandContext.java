package pers.neige.neigeitems.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandContext {
    @NotNull
    private final CommandSender sender;
    @NotNull
    private final List<String> keys;
    @NotNull
    private String[] args;
    private int index;
    @Nullable
    private String arg;

    public CommandContext(
            @NotNull CommandSender sender,
            @NotNull String[] args
    ) {
        this.sender = sender;
        this.args = args;
        this.index = -1;
        this.arg = null;
        this.keys = new ArrayList<>();
    }

    public CommandContext(
            @NotNull CommandSender sender,
            @NotNull String[] args,
            int index
    ) {
        this.sender = sender;
        this.args = args;
        this.index = index;
        if (args.length > index) {
            this.arg = args[index];
        }
        this.keys = new ArrayList<>();
    }

    public CommandContext(
            @NotNull CommandSender sender,
            @NotNull String[] args,
            int index,
            @NotNull List<String> keys
    ) {
        this.sender = sender;
        this.args = args;
        this.index = index;
        if (args.length > index) {
            this.arg = args[index];
        }
        this.keys = keys;
    }

    @Nullable
    public String get(String key) {
        int index = keys.indexOf(key);
        if (index != -1) {
            return args[index];
        }
        return null;
    }

    protected CommandContext push(
            @NotNull String key
    ) {
        keys.add(key);
        index++;
        if (args.length > index) {
            arg = args[index];
        } else {
            arg = "";
        }
        return this;
    }

    @NotNull
    public CommandSender getSender() {
        return sender;
    }

    @NotNull
    public String[] getArgs() {
        return args;
    }

    protected void setArgs(
            @NotNull String[] args
    ) {
        this.args = args;
    }

    public int getIndex() {
        return index;
    }

    @Nullable
    public String getArg() {
        return arg;
    }

    protected void setArg(
            @Nullable String arg
    ) {
        this.arg = arg;
    }

    @NotNull
    public List<String> getKeys() {
        return keys;
    }
}
