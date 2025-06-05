package pers.neige.neigeitems.utils;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CommandUtils {
    private static SimpleCommandMap commandMap;

    private static Map<String, Command> knownCommands;

    private static Constructor<PluginCommand> constructor;

    private static Field ownPluginField;

    static {
        val pluginManager = (SimplePluginManager) Bukkit.getPluginManager();
        Field commandMapField;
        try {
            commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Field knownCommandsField;
        try {
            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            ownPluginField = PluginCommand.class.getDeclaredField("owningPlugin");
            ownPluginField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建一个 PluginCommand 对象.
     *
     * @param name  指令名.
     * @param owner 创建指令的插件.
     * @return 对应的 PluginCommand 对象.
     */
    public static @Nullable PluginCommand newPluginCommand(
            @NonNull String name,
            @NonNull Plugin owner
    ) {
        try {
            return constructor.newInstance(name, owner);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 SimpleCommandMap.
     *
     * @return SimpleCommandMap.
     */
    public static @NonNull SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    /**
     * 获取所有已注册指令.
     *
     * @return 所有已注册指令.
     */
    public static @NonNull Map<String, Command> getKnownCommands() {
        return knownCommands;
    }

    /**
     * 卸载指令.
     *
     * @param command 待卸载指令.
     */
    public static void unregisterCommand(
            String command
    ) {
        getKnownCommands().remove(command);
    }

    /**
     * 设置指令拥有者.
     *
     * @param command 待设置指令.
     */
    public static void setPlugin(
            PluginCommand command,
            Plugin plugin
    ) {
        try {
            ownPluginField.set(command, plugin);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        command.setExecutor(plugin);
    }
}
