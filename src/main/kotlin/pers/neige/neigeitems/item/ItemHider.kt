package pers.neige.neigeitems.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.entity.Item
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ

/**
 * 用于实现掉落物隐藏功能
 */
class ItemHider {
    init {
        val protocolManager = ProtocolLibrary.getProtocolManager()
        protocolManager.addPacketListener(object : PacketAdapter(
            NeigeItems.getInstance(), ListenerPriority.LOWEST, PacketType.Play.Server.ENTITY_METADATA
        ) {
            override fun onPacketSending(event: PacketEvent) {
                val player = event.player
                val packet = event.packet.handle
                val id = PacketUtils.getEntityIdFromPacketPlayOutEntityMetadata(packet)
                if (id < 0) return
                val entity = WorldUtils.getEntityFromID(player.world, id) ?: return
                if (entity !is Item || !entity.hasMetadata("NI-Owner")) return
                // 获取归属者
                val owner = entity.getMetadataEZ("NI-Owner", "") as String
                // 检测拾取者是否是拥有者以及是否隐藏掉落物
                event.isCancelled = entity.getScoreboardTags().contains("NI-Hide") && player.name != owner
            }
        })
    }
}