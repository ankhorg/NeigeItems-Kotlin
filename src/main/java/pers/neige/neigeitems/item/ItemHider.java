package pers.neige.neigeitems.item;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.utils.PlayerUtils;

public class ItemHider {
    public ItemHider() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(
                NeigeItems.getInstance(),
                ListenerPriority.LOWEST,
                PacketType.Play.Server.ENTITY_METADATA
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                Object packet = event.getPacket().getHandle();
                int id = PacketUtils.getEntityIdFromPacketPlayOutEntityMetadata(packet);
                if (id < 0) return;
                Entity entity = WorldUtils.getEntityFromIDAsync(player.getWorld(), id);
                if (entity == null) return;
                if (!(entity instanceof Item) || !entity.hasMetadata("NI-Owner")) return;
                // 获取归属者
                String owner = (String) PlayerUtils.getMetadataEZ(entity, "NI-Owner", "");
                // 检测拾取者是否是拥有者以及是否隐藏掉落物
                event.setCancelled(entity.getScoreboardTags().contains("NI-Hide") && !player.getName().equals(owner));
            }
        });
    }
}
