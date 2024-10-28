package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerJoinEvent
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemColor
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils
import pers.neige.neigeitems.network.ChannelHandler
import pers.neige.neigeitems.task.Updater
import pers.neige.neigeitems.utils.LangUtils.sendLang

object PlayerJoinListener {
    @JvmStatic
    @Listener
    private fun listener(event: PlayerJoinEvent) {
        val channel = EntityPlayerUtils.getChannel(event.player)!!
        val handler = ChannelHandler(event.player.uniqueId)
        channel.eventLoop().submit {
            channel.pipeline().addBefore("packet_handler", "neigeitems_packet_handler", handler)
        }

        ItemColor.initTeam(event.player)

        NeigeItems.getUserManager().getOrMake(event.player.uniqueId)
        if (!event.player.isOp) return
        if (Updater.latestVersion == null || Updater.latestVersion == Updater.currentVersion) return
        event.player.sendLang(
            "Messages.findNewVersion", mapOf(
                Pair("{version}", Updater.latestVersion!!)
            )
        )
        event.player.sendLang(
            "Messages.updateLink", mapOf(
                Pair("{link}", "https://github.com/ankhorg/NeigeItems-Kotlin/releases/latest")
            )
        )
    }
}