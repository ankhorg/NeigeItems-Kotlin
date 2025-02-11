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
import org.inksnow.cputil.logger.AuroraLoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtItemStack;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.scanner.ClassScanner;

import java.io.File;

public class NeigeItems extends JavaPlugin {
    private static final Logger logger;
    private static NeigeItems INSTANCE;

    static {
        AuroraLoggerFactory.instance().nameMapping(it -> "NeigeItems " + it);
        logger = LoggerFactory.getLogger(NeigeItems.class.getSimpleName());
        try {
            logger.info("loading ankh-invoke");
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
            logger.info("ankh-invoke loaded");
        } catch (Throwable e) {
            logger.error("failed to load ankh-invoke", e);
        }
    }

    private ClassScanner scanner = null;

    public NeigeItems() {
        super();
        onInit();
    }

    public NeigeItems(
            @NotNull JavaPluginLoader loader,
            @NotNull PluginDescriptionFile description,
            @NotNull File dataFolder,
            @NotNull File file
    ) {
        super(loader, description, dataFolder, file);
        onInit();
    }

    public static NeigeItems getInstance() {
        return INSTANCE;
    }

    public void onInit() {
        try {
            if (!CbVersion.v1_20_R4.isSupport()) {
                ItemStack itemStack = new ItemStack(Material.STONE);
                NbtItemStack nbtItemStack = new NbtItemStack(itemStack);
                NbtCompound nbt = nbtItemStack.getOrCreateTag();
                nbt.putString("test", "test");
                nbt.getString("test");
            }
        } catch (Throwable error) {
            logger.warn("插件NBT前置库未正常加载依赖, 本插件不支持包括但不限于 Mohist/Catserver/Arclight 等混合服务端, 对于每个大版本, 本插件仅支持最新小版本, 如支持 1.19.4 但不支持 1.19.2, 请选用正确的服务端, 或卸载本插件");
            logger.warn("The plugin's NBT pre-requisite library failed to load. This plugin does not support mixed server platforms including but not limited to Mohist/Catserver/Arclight, etc. For each major version, this plugin only supports the latest minor version. For example, it supports 1.19.4 but not 1.19.2. Please use the correct server platform or uninstall this plugin", error);

            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.disablePlugin(this);
            return;
        }

        boolean safe = checkMagicUtils("DamageEventUtils");

        if (!checkMagicUtils("EnchantmentUtils")) safe = false;
        if (!checkMagicUtils("EntityItemUtils")) safe = false;
        if (!checkMagicUtils("EntityPlayerUtils")) safe = false;
        if (!checkMagicUtils("EntityUtils")) safe = false;
        if (!checkMagicUtils("InventoryUtils")) safe = false;
        if (!checkMagicUtils("MathUtils")) safe = false;
        if (!checkMagicUtils("PacketUtils")) safe = false;
        if (!checkMagicUtils("PaperInventoryUtils")) safe = false;
        if (!checkMagicUtils("ServerUtils")) safe = false;
        if (!checkMagicUtils("SpigotInventoryUtils")) safe = false;
        if (!checkMagicUtils("TranslationUtils")) safe = false;
        if (!checkMagicUtils("WorldUtils")) safe = false;

        if (!safe) {
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.disablePlugin(this);
            return;
        }
        INSTANCE = this;
        scanner = new ClassScanner(this);
    }

    @Override
    public void onLoad() {
        scanner.onLoad();
    }

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        scanner.onEnable();
    }

    @Override
    public void onDisable() {
        if (scanner != null) {
            scanner.onDisable();
        }
    }

    private boolean checkMagicUtils(String className) {
        try {
            Class.forName("pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils." + className);
            return true;
        } catch (Throwable error) {
            logger.warn("{} 类未正常加载, 这可能造成不可预知的错误, 请联系作者修复", className);
            logger.warn("class {} did not load properly, which may cause unpredictable errors. Please contact the author for repair.", className, error);
            return false;
        }
    }
}
