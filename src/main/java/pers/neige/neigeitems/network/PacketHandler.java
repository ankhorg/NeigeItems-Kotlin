package pers.neige.neigeitems.network;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.event.ItemPacketEvent;
import pers.neige.neigeitems.item.ItemColor;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils;
import pers.neige.neigeitems.utils.ItemUtils;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class PacketHandler {
    private static final Map<String, BiFunction<UUID, Object, Boolean>> packetSendHandlers = new ConcurrentHashMap<>();
    private static final Map<String, BiFunction<UUID, Object, Boolean>> packetReceiveHandlers = new ConcurrentHashMap<>();

    static {
        packetSendHandlers.put(PacketUtils.SET_SLOT, PacketHandler::handleSetSlotPacket);
        packetSendHandlers.put(PacketUtils.WINDOW_ITEMS, PacketHandler::handleWindowItemsPacket);
        packetSendHandlers.put(PacketUtils.ENTITY_METADATA, PacketHandler::handleEntityMetadataPacket);
    }

    public static Map<String, BiFunction<UUID, Object, Boolean>> getPacketSendHandlers() {
        return packetSendHandlers;
    }

    public static Map<String, BiFunction<UUID, Object, Boolean>> getPacketReceiveHandlers() {
        return packetReceiveHandlers;
    }

    public static boolean handleSetSlotPacket(@NotNull UUID uuid, @NotNull Object packet) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) return true;
        ItemStack itemStack = PacketUtils.getItemStackFromPacketPlayOutSetSlot(packet);
        if (itemStack == null || itemStack.getType() == Material.AIR) return true;
        return new ItemPacketEvent(player, itemStack).call();
    }

    public static boolean handleWindowItemsPacket(@NotNull UUID uuid, @NotNull Object packet) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) return true;
        List<ItemStack> itemStacks = PacketUtils.getItemsFromPacketPlayOutWindowItems(packet);
        if (itemStacks != null) {
            itemStacks.forEach((itemStack) -> {
                if (itemStack == null || itemStack.getType() == Material.AIR) return;
                new ItemPacketEvent(player, itemStack).call();
            });
        }
        ItemStack itemStack = PacketUtils.getCarriedItemFromPacketPlayOutWindowItems(packet);
        if (itemStack == null || itemStack.getType() == Material.AIR) return true;
        return new ItemPacketEvent(player, itemStack).call();
    }

    public static boolean handleEntityMetadataPacket(@NotNull UUID uuid, @NotNull Object packet) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        int id = PacketUtils.getEntityIdFromPacketPlayOutEntityMetadata(packet);
        if (id < 0) return true;
        Entity entity = WorldUtils.getEntityFromID1(player.getWorld(), id);
        if (!(entity instanceof Item)) return true;
        boolean result = true;
        // 因为某种未知的原因, 高版本拦完EntityMetadata没用
        // 但用ProtocolLib拦就有用, 非常的无法理解
//        if (entity.hasMetadata("NI-Owner")) {
//            String owner = (String) PlayerUtils.getMetadataEZ(entity, "NI-Owner", "");
//            result = !entity.getScoreboardTags().contains("NI-Hide") || player.getName().equals(owner);
//        }
        ItemStack itemStack = ((Item) entity).getItemStack();
        NbtCompound nbt = ItemUtils.getNbtOrNull(itemStack);
        if (nbt == null) return result;
        String colorString = nbt.getDeepString("NeigeItems.color");
        if (colorString == null) return result;
        ChatColor color = ItemColor.getColors().get(colorString);
        if (color == null) return result;
        entity.setGlowing(true);
        ItemColor.initTeam(player);
        Team team = player.getScoreboard().getTeam("NI-" + color);
        if (team == null) return result;
        Object teamPacket = PacketUtils.newScoreboardTeamPacket(team, entity.getUniqueId());
        SchedulerUtils.asyncLater(0, () -> {
            EntityPlayerUtils.sendPacket(player, teamPacket);
        });
        return result;
    }
}
