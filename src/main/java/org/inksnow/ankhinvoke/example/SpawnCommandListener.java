package org.inksnow.ankhinvoke.example;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inksnow.ankhinvoke.example.ref.craftbukkit.RefCraftWorld;
import org.inksnow.ankhinvoke.example.ref.nms.*;

public class SpawnCommandListener implements Listener {
  private static final boolean TEST1 = CbVersion.v1_14_R1.isSupport();
  private static final boolean TEST2 = CbVersion.v1_13_R1.isSupport();

//  private static RefNewEntityTypes<NewNoAiSheep> SHEEP = null;

  private final JavaPlugin plugin;

  public SpawnCommandListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  static {
    if (TEST2) {
//      SHEEP = RefNewEntityTypes.registerEntity("no_ai_sheep", RefEntityTypesBuilder.of(NewNoAiSheep::new, RefMobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10));
    } else {
//      RefEntityTypes.registerEntity(91, "no_ai_sheep", NoAiSheep.class, "No Ai Sheep");
    }
  }

  @EventHandler
  public void on(AsyncPlayerChatEvent event) {
    if (event.getMessage().equals("!spawn")) {
      Bukkit.getScheduler().runTask(plugin, () -> {

        World bukkitWorld = event.getPlayer().getWorld();
        RefWorldServer serverWorld = ((RefCraftWorld) bukkitWorld).getHandle();

        Location bukkitLocation = event.getPlayer().getLocation();
        if (TEST1) {
          NewNoAiSheep sheep = new NewNoAiSheep(RefNewEntityTypes.SHEEP, serverWorld);
          sheep.setPosition(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ());
          serverWorld.addEntity(sheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } else {
//          NoAiSheep sheep = new NoAiSheep(serverWorld);
//          sheep.setPosition(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ());
//          serverWorld.addEntity(sheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
        }

        event.getPlayer().sendMessage("spawn sheep");
      });
    }
  }
}
