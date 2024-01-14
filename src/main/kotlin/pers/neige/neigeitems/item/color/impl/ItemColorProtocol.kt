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
import org.bukkit.ChatColor
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Scoreboard
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.color.ItemColor
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ListenerUtils
import java.util.*


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

    private val checkedScoreboard = HashSet<Scoreboard>()

    /**
     * 根据玩家当前计分板进行Team初始化.
     * 玩家的计分板不一定一成不变,
     * 可能刚进服时玩家是主计分板,
     * 过一段时间其他插件又根据需要切换了玩家的计分板.
     * 需要保证每个计分板内都存在对应的Team,
     * 不然玩家的客户端将认为掉落物所在的Team与玩家无关,
     * 从而导致掉落物只发出白色光效.
     *
     * @param player 待操作玩家
     */
    fun initTeam(player: Player) {
        if (!checkedScoreboard.contains(player.scoreboard)) {
            for ((id, color) in colors) {
                // 注册Team
                var team = player.scoreboard.getTeam("NIProtocol-$color")
                team?.unregister()
                team = player.scoreboard.registerNewTeam("NIProtocol-$color")
                // 1.13+设置color即可改变光效发光颜色
                team.color = color
                // 1.12-需要给prefix设置颜色才能改变发光颜色
                team.prefix = color.toString()
            }
            checkedScoreboard.add(player.scoreboard)
        }
    }

    init {
        // 玩家登录时根据玩家当前计分板进行Team初始化
        ListenerUtils.registerListener(PlayerJoinEvent::class.java) {
            initTeam(it.player)
        }

        // 用于设置物品发光颜色
        protocolManager.addPacketListener(object :
            PacketAdapter(
                plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_METADATA
            ) {
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
                        val itemInfo = itemStack.isNiItem() ?: return
                        val colorString = itemInfo.neigeItems.getString("color") ?: return
                        val color = colors[colorString] ?: return

                        // 设置发光
                        // 使用发包让实体发光只会带来无穷的麻烦
                        // 直接通过BukkitAPI设置发光效果才会让我们走向美好的未来
                        entity.isGlowing = true
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
                                internalStructure.getEnumModifier(ChatColor::class.java, enumChatFormatClass)
                                    .write(0, color)
                                packet.optionalStructures.write(0, Optional.of(internalStructure))
                            }
                        }
                        // 设置队伍ID
                        packet.strings.write(0, "NIProtocol-$color")
                        // 添加实体
                        (packet.getSpecificModifier(Collection::class.java)
                            .read(0) as ArrayList<String>).add(entity.uniqueId.toString())
                        // 发送数据包
                        protocolManager.sendServerPacket(receiver, packet)
                    }
                }
            }

            override fun onPacketReceiving(event: PacketEvent) {}
        }
        )
    }
}