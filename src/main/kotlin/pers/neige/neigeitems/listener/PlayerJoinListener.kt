package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerJoinEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemColor
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils
import pers.neige.neigeitems.manager.UserManager
import pers.neige.neigeitems.network.ChannelHandler
import pers.neige.neigeitems.task.Updater
import pers.neige.neigeitems.utils.LangUtils.sendLang

object PlayerJoinListener {
    @JvmStatic
    @Listener
    private fun listener(event: PlayerJoinEvent) {
        // 数据包监听
        val channel = EntityPlayerUtils.getChannel(event.player)!!
        val handler = ChannelHandler(event.player.uniqueId)
        channel.eventLoop().submit {
            channel.pipeline().addBefore("packet_handler", "neigeitems_packet_handler", handler)
        }
        // 初始化掉落物颜色Team
        ItemColor.initTeam(event.player)
        // 初始化User
        UserManager.INSTANCE.create(event.player.uniqueId)
        // 为op发送更新检测信息
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