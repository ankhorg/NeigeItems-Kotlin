package pers.neige.neigeitems.item.color.impl

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.reflect.FieldAccessException
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.color.ItemColor
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.module.nms.getItemTag
import java.util.*
import kotlin.experimental.or


/**
 * 基于ProtocolLib功能实现的掉落物光效系统
 *
 * @constructor 启用基于ProtocolLib功能实现的掉落物光效系统
 */
class ItemColorProtocol : ItemColor() {
    override val mode = "Protocol"

    private val enumChatFormatClass: Class<*> = MinecraftReflection.getMinecraftClass("EnumChatFormat")

    private val protocolManager: ProtocolManager = ProtocolLibrary.getProtocolManager()

    private val version = let {
        val version = MinecraftVersion.getCurrentVersion()
        when {
            !version.isAtLeast(MinecraftVersion.AQUATIC_UPDATE) -> 12
            !version.isAtLeast(MinecraftVersion.CAVES_CLIFFS_1) -> 13
            else -> 17
        }
    }

    private val teamPackets = let {
        val teamPackets = HashMap<String, PacketContainer>()
        for ((id, color) in colors) {
            // 创建Team数据包
            val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
            // 设置队伍ID
            packet.strings.write(0, "NI-$color")
            when (version) {
                // 1.12版本, 设置前缀即可显示颜色
                12 -> {
                    // 设置数据包类型为"创建Team"
                    packet.integers.write(1, 0)
                    // 设置队伍前缀
                    packet.strings.write(2, color.toString())
                }
                // 1.13-1.16, 设置颜色即可显示颜色
                13 -> {
                    // 设置数据包类型为"创建Team"
                    packet.integers.write(0, 0)
                    // 设置队伍颜色
                    packet.getEnumModifier(ChatColor::class.java, enumChatFormatClass).write(0, color)
                }
                // 1.17+, 重点仍旧在设置颜色, 但是创建数据包时PacketPlayOutScoreboardTeam$b是没有默认值的, 需要一个个手动填满
                else -> {
                    // 设置数据包类型为"创建Team"
                    packet.integers.write(0, 0)
                    // 获取队伍信息
                    val internalStructure = packet.optionalStructures.read(0).get()
                    // 设置队伍颜色
                    internalStructure.getEnumModifier(ChatColor::class.java, enumChatFormatClass).write(0, color)
                    // 设置展示名
                    internalStructure.chatComponents.write(0, WrappedChatComponent.fromText("NI-$color"))
                    // 设置前缀
                    internalStructure.chatComponents.write(1, WrappedChatComponent.fromText(color.toString()))
                    // 设置后缀
                    internalStructure.chatComponents.write(2, WrappedChatComponent.fromText(""))
                    // 设置名牌可见性
                    internalStructure.strings.write(0, "always")
                    // 设置TeamPush
                    internalStructure.strings.write(1, "always")
                    // 设置友军选项
                    internalStructure.integers.write(0, 3)
                }
            }
            // 存储数据包
            teamPackets[id] = packet
        }
        teamPackets
    }

    /**
     * 根据玩家当前计分板进行Team初始化
     * 玩家的计分板不一定一成不变,
     * 可能刚进服时玩家是主计分板,
     * 过一段时间其他插件又根据需要切换了玩家的计分板.
     * 这样就需要重新向玩家发送Team数据包,
     * 不然玩家的客户端将认为之前的Team与玩家无关,
     * 从而导致掉落物只发出白色光效.
     *
     * @param player 待操作玩家
     */
    fun initTeam(player: Player) {
        // 每次发包后会通过Metadata记录玩家当前所处计分板,
        // 没有相关Metadata说明从未初始化, Metadata不符说明计分板发生了切换, 都需要重新进行Team初始化.
        // 直接scoreboard.toString()可以暴露当前Scoreboard的指针, 从而正确判断是否为同一个计分板.
        if (!player.hasMetadata("NI-TeamScoreboard") || (player.getMetadataEZ("NI-TeamScoreboard", "String", "") as String) != player.scoreboard.toString()) {
            // 初始化前通过Metadata记录当前计分板
            player.setMetadataEZ("NI-TeamScoreboard", player.scoreboard.toString())
            for ((id, packet) in teamPackets) {
                // 发送数据包
                protocolManager.sendServerPacket(player, packet)
            }
        }
    }

    init {
        // 玩家登录时根据玩家当前计分板进行Team初始化
        registerBukkitListener(PlayerJoinEvent::class.java, EventPriority.NORMAL, false) {
            bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                initTeam(it.player)
            })
        }

        // 用于设置物品发光颜色
        protocolManager.addPacketListener(object :
            PacketAdapter(
                plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_METADATA) {
                override fun onPacketSending(event: PacketEvent) {
                    // 数据包接收者
                    val receiver = event.player
                    // 相关实体
                    val id = event.packet.integers.read(0)
                    if (id >= 0) {
                        // 虽然但是, 有的时候会找不到相关实体
                        val entity = try {
                            protocolManager.getEntityFromID(receiver.world, id)
                        } catch (error: FieldAccessException) {
                            null
                        }
                        if (entity is Item) {
                            val itemStack = entity.itemStack
                            if (itemStack.type != Material.AIR) {
                                val itemTag = itemStack.getItemTag()
                                // 检测物品是否有用于标记光效颜色的特殊NBT
                                itemTag["NeigeItems"]?.asCompound()?.get("color")?.asString()?.let {
                                    colors[it]?.let { color ->
                                        // 设置发光
                                        val flag = event.packet.watchableCollectionModifier.read(0)[0]
                                        val byte = flag.value
                                        if (byte is Byte) {
                                            flag.value = byte or (1 shl 6)
                                        }
                                        bukkitScheduler.runTaskAsynchronously(NeigeItems.plugin, Runnable {
                                            initTeam(event.player)
                                            // 创建Team数据包
                                            val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
                                            // 设置数据包类型为"向Team添加实体"
                                            when (version) {
                                                12 -> {
                                                    packet.integers.write(1, 3)
                                                }
                                                13 -> {
                                                    packet.integers.write(0, 3)
                                                }
                                                else -> {
                                                    packet.integers.write(0, 3)
                                                    // 设置队伍颜色
                                                    val internalStructure = packet.optionalStructures.read(0).get()
                                                    internalStructure.getEnumModifier(ChatColor::class.java, enumChatFormatClass).write(0, color)
                                                    packet.optionalStructures.write(0, Optional.of(internalStructure))
                                                }
                                            }
                                            // 设置队伍ID
                                            packet.strings.write(0, "NI-$color")
                                            // 添加实体
                                            (packet.getSpecificModifier(Collection::class.java).read(0) as ArrayList<String>).add(entity.uniqueId.toString())
                                            // 发送数据包
                                            protocolManager.sendServerPacket(receiver, packet)
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
                override fun onPacketReceiving(event: PacketEvent) {}
            }
        )
    }
}