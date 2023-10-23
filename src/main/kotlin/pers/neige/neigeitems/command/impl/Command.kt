package pers.neige.neigeitems.command.impl

import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.command.CommandDispatcher
import pers.neige.neigeitems.command.impl.sub.Give
import pers.neige.neigeitems.command.subcommand.Help
import pers.neige.neigeitems.utils.SchedulerUtils.async

object Command {
    var dispatcher = CommandDispatcher("NeigeItemsTest")

    init {
        dispatcher
            .execute {
                async {
                    Help.help(it.sender)
                }
            }
            .then(Give.get)
    }

//    @Awake(lifeCycle = Awake.LifeCycle.ENABLE)
    fun register() {
        dispatcher.register(arrayListOf("nitest"))
    }
}