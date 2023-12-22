package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.ref.entity.RefCraftPlayer;
import pers.neige.neigeitems.ref.entity.RefEntityPlayer;

public class DamageEventUtils {
    /**
     * 获取事件是否造成暴击伤害.
     *
     * @param event 待检测事件.
     */
    public static boolean isCritical(
            @NotNull EntityDamageByEntityEvent event
    ) {
        // 检测玩家攻击情况
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return false;
        // 获取攻击者
        Entity attacker = event.getDamager();
        // 检测攻击者是否是玩家
        if (!((Object) attacker instanceof RefCraftPlayer)) return false;
        Player player = (Player) attacker;
        RefEntityPlayer entityPlayer = ((RefCraftPlayer) (Object) attacker).getHandle();
        // 蓄力大于90%
        return EntityPlayerUtils.getAttackCooldown(player) > 0.9f
                // 正在下落
                && player.getFallDistance() > 0.0f
                // 腿没着地
                && !player.isOnGround()
                // 脚没蹬梯子/藤蔓上
                && !entityPlayer.onClimbable()
                // 没淌水里
                && !entityPlayer.isInWater()
                // 没瞎
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS)
                // 没骑东西
                && !entityPlayer.isPassenger()
                // 对方是个活的
                && event.getEntity() instanceof LivingEntity;
    }

    /**
     * 获取事件是否通过盾牌格挡伤害.
     *
     * @param event 待检测事件.
     */
    public static boolean isBlocking(
            @NotNull EntityDamageEvent event
    ) {
        // 获取受击者
        Entity entity = event.getEntity();
        // 检测是否为玩家
        if (!(entity instanceof Player)) return false;
        // 检测是否存在格挡行为
        return event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) != 0.0;
    }
}
