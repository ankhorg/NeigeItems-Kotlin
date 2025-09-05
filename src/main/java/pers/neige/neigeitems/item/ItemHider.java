package pers.neige.neigeitems.item;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.val;
import org.bukkit.entity.Item;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.utils.PlayerUtils;

public class ItemHider {
    public ItemHider() {
        val protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(
                NeigeItems.getInstance(),
                ListenerPriority.LOWEST,
                PacketType.Play.Server.ENTITY_METADATA
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                val player = event.getPlayer();
                val packet = event.getPacket().getHandle();
                val id = PacketUtils.getEntityIdFromPacketPlayOutEntityMetadata(packet);
                if (id < 0) return;
                val entity = HookerManager.INSTANCE.getNmsHooker().getEntityFromID1(player.getWorld(), id);
                if (entity == null) return;
                if (!(entity instanceof Item) || !entity.hasMetadata("NI-Owner")) return;
                // 获取归属者
                val owner = (String) PlayerUtils.getMetadataEZ(entity, "NI-Owner", "");
                // 检测拾取者是否是拥有者以及是否隐藏掉落物
                event.setCancelled(entity.getScoreboardTags().contains("NI-Hide") && !player.getName().equals(owner));
            }
        });
    }
}
