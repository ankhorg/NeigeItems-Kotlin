package pers.neige.neigeitems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.inksnow.ankhinvoke.bukkit.AnkhInvokeBukkit;
import org.inksnow.ankhinvoke.bukkit.injector.JarTransformInjector;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.lang.LocaleI18n;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtItemStack;
import pers.neige.neigeitems.manager.ConfigManager;
import pers.neige.neigeitems.scanner.ClassScanner;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;

public class NeigeItems extends JavaPlugin {
    private static NeigeItems INSTANCE;

    static {
        init();
    }

    private ClassScanner scanner = null;

    public NeigeItems() {
        super();
        INSTANCE = this;
    }

    public NeigeItems(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        INSTANCE = this;
    }

    public static NeigeItems getInstance() {
        return INSTANCE;
    }

    public static void init() {
        try {
            System.out.println("[NeigeItems] loading ankh-invoke");
            AnkhInvokeBukkit.forBukkit(NeigeItems.class)
                    .reference()
                    .appendPackage("pers.neige.neigeitems.ref")
                    .build()
                    .inject()
                    .injector(
                            new JarTransformInjector.Builder()
                                    .classLoader(NeigeItems.class.getClassLoader())
                                    .transformPackage("pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.")
                                    .build()
                    )
                    .build()
                    .referenceRemap()
                    .setApplyMapRegistry("neigeitems")
                    .build()
                    .build();
            System.out.println("[NeigeItems] ankh-invoke loaded");
        } catch (Throwable e) {
            System.out.println("[NeigeItems] failed to load ankh-invoke");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        try {
            ItemStack itemStack = new ItemStack(Material.STONE);
            NbtItemStack nbtItemStack = new NbtItemStack(itemStack);
            NbtCompound nbt = nbtItemStack.getOrCreateTag();
            nbt.putString("test", "test");
            nbt.getString("test");
        } catch (Throwable error) {
            NeigeItems.getInstance().getLogger().warning("插件NBT前置库未正常加载依赖, 本插件不支持包括但不限于 Mohist/Catserver/Arclight 等混合服务端, 对于每个大版本, 本插件仅支持最新小版本, 如支持 1.19.4 但不支持 1.19.2, 请选用正确的服务端, 或卸载本插件");
            NeigeItems.getInstance().getLogger().warning("The plugin's NBT pre-requisite library failed to load. This plugin does not support mixed server platforms including but not limited to Mohist/Catserver/Arclight, etc. For each major version, this plugin only supports the latest minor version. For example, it supports 1.19.4 but not 1.19.2. Please use the correct server platform or uninstall this plugin.");
            error.printStackTrace();
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.disablePlugin(this);
            return;
        }

        boolean safe = true;

        if (!checkMagicUtils("DamageEventUtils")) safe = false;
        if (!checkMagicUtils("EnchantmentUtils")) safe = false;
        if (!checkMagicUtils("EntityItemUtils")) safe = false;
        if (!checkMagicUtils("EntityPlayerUtils")) safe = false;
        if (!checkMagicUtils("EntityUtils")) safe = false;
        if (!checkMagicUtils("InventoryUtils")) safe = false;
        if (!checkMagicUtils("SpigotInventoryUtils")) safe = false;
        if (!checkMagicUtils("PaperInventoryUtils")) safe = false;
        if (!checkMagicUtils("ServerUtils")) safe = false;
        if (!checkMagicUtils("TranslationUtils")) safe = false;
        if (!checkMagicUtils("WorldUtils")) safe = false;

        if (!safe) {
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.disablePlugin(this);
            return;
        }
        scanner = new ClassScanner(
                this,
                NeigeItems.class.getPackage().getName(),
                new HashSet<>(Collections.singletonList(NeigeItems.class.getPackage().getName() + ".libs"))
        );
        scanner.onEnable();
    }

    @Override
    public void onDisable() {
        scanner.onDisable();
    }

    private boolean checkMagicUtils(String className) {
        try {
            Class.forName("pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils." + className);
            return true;
        } catch (Throwable error) {
            NeigeItems.getInstance().getLogger().warning(className + " 类未正常加载, 这可能造成不可预知的错误, 请联系作者修复");
            NeigeItems.getInstance().getLogger().warning("class " + className + " did not load properly, which may cause unpredictable errors. Please contact the author for repair.");
            error.printStackTrace();
            return false;
        }
    }
}
