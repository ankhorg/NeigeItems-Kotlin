package pers.neige.neigeitems.listener

import org.bukkit.GameMode
import org.bukkit.event.player.PlayerGameModeChangeEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.utils.SchedulerUtils.syncLater

object PlayerGameModeChangeListener {
    @JvmStatic
    @Listener
    private fun listener(event: PlayerGameModeChangeEvent) {
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE || event.newGameMode == GameMode.CREATIVE) {
            if (ConfigManager.config.getBoolean("ItemPlaceholder.enable")) {
                syncLater(1) {
                    player.updateInventory()
                }
            }
        }
    }
}