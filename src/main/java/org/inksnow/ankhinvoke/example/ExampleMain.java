package org.inksnow.ankhinvoke.example;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.inksnow.ankhinvoke.bukkit.AnkhInvokeBukkit;
import pers.neige.neigeitems.NeigeItems;

import java.net.URLClassLoader;

public class ExampleMain {
  public static void init() {
    try {
      AnkhInvokeBukkit.forBukkit((Class<? extends JavaPlugin>)Class.forName("pers.neige.neigeitems.taboolib.platform.BukkitPlugin"))
              .reference()
              /**/.appendPackage("org.inksnow.ankhinvoke.example.ref")
              /**/.build()
              .inject()
              /**/.urlInjector((URLClassLoader) Class.forName("pers.neige.neigeitems.taboolib.platform.BukkitPlugin").getClassLoader())
              /**/.build()
              .referenceRemap()
              /**/.setApplyMapRegistry("neigeitems")
              /**/.build()
              .build();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void onEnable() {
    Bukkit.getPluginManager().registerEvents(new SpawnCommandListener(NeigeItems.INSTANCE.getPlugin()), NeigeItems.INSTANCE.getPlugin());
  }
}
