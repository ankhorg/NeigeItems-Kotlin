package pers.neige.neigeitems.network;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import pers.neige.neigeitems.event.ItemPacketEvent;
import pers.neige.neigeitems.item.ItemColor;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.utils.ItemUtils;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class PacketHandler {
    private static final @NonNull Map<String, BiFunction<UUID, Object, Boolean>> packetSendHandlers = new ConcurrentHashMap<>();
    private static final @NonNull Map<String, BiFunction<UUID, Object, Boolean>> packetReceiveHandlers = new ConcurrentHashMap<>();

    static {
        packetSendHandlers.put(PacketUtils.SET_SLOT, PacketHandler::handleSetSlotPacket);
        packetSendHandlers.put(PacketUtils.WINDOW_ITEMS, PacketHandler::handleWindowItemsPacket);
        if (PacketUtils.ADD_CURSOR_ITEM_PACKET) {
            packetSendHandlers.put(PacketUtils.CURSOR_ITEM.get(), PacketHandler::handleCursorItemPacket);
        }
        packetSendHandlers.put(PacketUtils.ENTITY_METADATA, PacketHandler::handleEntityMetadataPacket);
    }

    public static Map<String, BiFunction<UUID, Object, Boolean>> getPacketSendHandlers() {
        return packetSendHandlers;
    }

    public static Map<String, BiFunction<UUID, Object, Boolean>> getPacketReceiveHandlers() {
        return packetReceiveHandlers;
    }

    public static boolean handleSetSlotPacket(@NonNull UUID uuid, @NonNull Object packet) {
        val player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        val gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) return true;
        val itemStack = PacketUtils.getItemStackFromPacketPlayOutSetSlot(packet);
        if (itemStack == null || itemStack.getType() == Material.AIR) return true;
        return new ItemPacketEvent(player, itemStack).call();
    }

    public static boolean handleCursorItemPacket(@NonNull UUID uuid, @NonNull Object packet) {
        val player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        val gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) return true;
        val itemStack = PacketUtils.getContentsFromClientboundSetCursorItemPacket(packet);
        if (itemStack == null || itemStack.getType() == Material.AIR) return true;
        return new ItemPacketEvent(player, itemStack).call();
    }

    public static boolean handleWindowItemsPacket(@NonNull UUID uuid, @NonNull Object packet) {
        val player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        val gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) return true;
        val itemStacks = PacketUtils.getItemsFromPacketPlayOutWindowItems(packet);
        if (itemStacks != null) {
            itemStacks.forEach((itemStack) -> {
                if (itemStack == null || itemStack.getType() == Material.AIR) return;
                new ItemPacketEvent(player, itemStack).call();
            });
        }
        val itemStack = PacketUtils.getCarriedItemFromPacketPlayOutWindowItems(packet);
        if (itemStack == null || itemStack.getType() == Material.AIR) return true;
        return new ItemPacketEvent(player, itemStack).call();
    }

    public static boolean handleEntityMetadataPacket(@NonNull UUID uuid, @NonNull Object packet) {
        val player = Bukkit.getPlayer(uuid);
        if (player == null) return true;
        val id = PacketUtils.getEntityIdFromPacketPlayOutEntityMetadata(packet);
        if (id < 0) return true;
        val entity = HookerManager.INSTANCE.getNmsHooker().getEntityFromID1(player.getWorld(), id);
        if (!(entity instanceof Item)) return true;
        boolean result = true;
        // 因为某种未知的原因, 高版本拦完EntityMetadata没用
        // 但用ProtocolLib拦就有用, 非常的无法理解
//        if (entity.hasMetadata("NI-Owner")) {
//            String owner = (String) PlayerUtils.getMetadataEZ(entity, "NI-Owner", "");
//            result = !entity.getScoreboardTags().contains("NI-Hide") || player.getName().equals(owner);
//        }
        val itemStack = ((Item) entity).getItemStack();
        val nbt = ItemUtils.getNbtOrNull(itemStack);
        if (nbt == null) return result;
        val colorString = nbt.getDeepString("NeigeItems.color");
        if (colorString == null) return result;
        val color = ItemColor.getColors().get(colorString);
        if (color == null) return result;
        entity.setGlowing(true);
        ItemColor.initTeam(player);
        val team = player.getScoreboard().getTeam("NI-" + color);
        if (team == null) return result;
        val teamPacket = PacketUtils.newScoreboardTeamPacket(team, entity.getUniqueId());
        SchedulerUtils.asyncLater(0, () -> {
            EntityPlayerUtils.sendPacket(player, teamPacket);
        });
        return result;
    }
}
