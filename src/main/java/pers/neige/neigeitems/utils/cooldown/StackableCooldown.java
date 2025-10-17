package pers.neige.neigeitems.utils.cooldown;

import lombok.*;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 累计次数冷却状态
 */
public class StackableCooldown {
    /**
     * 所有操作的同步锁
     */
    @Getter
    private final @NonNull ReentrantLock lock = new ReentrantLock();
    /**
     * 冷却配置
     */
    @Getter
    private final @NonNull StackableCooldown.Config config;
    /**
     * 当前剩余次数
     */
    private int amount;
    /**
     * 下次恢复时间
     */
    private long nextRecoveryTime;

    StackableCooldown(@NonNull StackableCooldown.Config config) {
        this.config = config;
        this.amount = config.initialAmount;
        if (config.maxAmount == config.initialAmount) {
            this.nextRecoveryTime = 0;
        } else {
            this.nextRecoveryTime = currentTimeMillis() + config.cooldown;
        }
    }

    /**
     * 这是为了允许 mockito 模拟.
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 状态更新
     */
    private void refresh() {
        // 满次数无需操作
        if (amount == config.maxAmount) {
            return;
        }
        // 当前次数
        val time = currentTimeMillis();
        while (true) {
            // 不可恢复, 中止操作
            if (nextRecoveryTime > time) {
                break;
            }
            // 恢复一次
            amount++;
            // 次数未满, 时间后移
            if (amount < config.maxAmount) {
                nextRecoveryTime += config.cooldown;
            } else {
                // 次数已满, 终止操作
                break;
            }
        }
    }

    /**
     * @return 剩余使用次数
     */
    public int getAmount() {
        lock.lock();
        try {
            refresh();
            return amount;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置使用次数
     */
    public void setAmount(int amount) {
        lock.lock();
        try {
            refresh();
            val preAmount = this.amount;
            // 无变化则中止操作
            if (preAmount == amount) return;
            this.amount = Math.max(0, Math.min(amount, config.maxAmount));
            // 满次数修改刷新时间
            if (this.amount == config.maxAmount) {
                nextRecoveryTime = currentTimeMillis();
                // 由满次数扣除, 开启冷却计时
            } else if (preAmount == config.maxAmount) {
                nextRecoveryTime = currentTimeMillis() + config.cooldown;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return 下次刷新时间
     */
    public long getNextRecoveryTime() {
        lock.lock();
        try {
            refresh();
            return nextRecoveryTime;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return 距下次刷新还有多久
     */
    public long getRemainingRecoveryTime() {
        lock.lock();
        try {
            refresh();
            return Math.max(0, nextRecoveryTime - currentTimeMillis());
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return 当前冷却状态
     */
    public @NonNull StackableCooldown.Status getCooldownStatus() {
        lock.lock();
        try {
            refresh();
            return new Status(amount, nextRecoveryTime);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试消耗使用次数
     *
     * @return 使用结果
     */
    public @NonNull ConsumeResult consume() {
        return consume(1);
    }

    /**
     * 尝试消耗使用次数
     *
     * @param amount 需要消耗的次数
     * @return 使用结果
     */
    public @NonNull ConsumeResult consume(int amount) {
        lock.lock();
        try {
            refresh();
            // 使用次数不足
            if (this.amount < amount) {
                return new ConsumeResult(false, new Status(this.amount, nextRecoveryTime));
            }
            // 满次数修改刷新时间
            if (this.amount == config.maxAmount) {
                nextRecoveryTime = currentTimeMillis() + config.cooldown;
            }
            // 次数扣除
            this.amount -= amount;
            return new ConsumeResult(true, new Status(this.amount, nextRecoveryTime));
        } finally {
            lock.unlock();
        }
    }

    /**
     * 增加或减少使用次数(取决于 amount 大于 0 还是小于 0)
     */
    public void addOrTakeAmount(int amount) {
        if (amount == 0) return;
        lock.lock();
        try {
            refresh();
            val preAmount = this.amount;
            this.amount = Math.max(0, Math.min(this.amount + amount, config.maxAmount));
            // 满次数修改刷新时间
            if (this.amount == config.maxAmount) {
                nextRecoveryTime = currentTimeMillis();
                // 由满次数扣除, 开启冷却计时
            } else if (preAmount == config.maxAmount) {
                nextRecoveryTime = currentTimeMillis() + config.cooldown;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置冷却
     */
    public void setCooldown(long cooldown) {
        lock.lock();
        try {
            refresh();
            // 满次数中止操作
            if (amount == config.maxAmount) return;
            val time = currentTimeMillis();
            // 待设置的冷却小于等于 0 则直接刷新次数
            if (cooldown <= 0) {
                amount++;
                if (amount == config.maxAmount) {
                    nextRecoveryTime = time;
                } else {
                    nextRecoveryTime = time + config.cooldown;
                }
            } else {
                // 重新设置刷新时间
                nextRecoveryTime = time + Math.max(0, Math.min(config.cooldown, cooldown));
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 延长或缩减冷却(cooldown 大于 0 延长, 反之缩减)<br>
     * 缩减冷却可以增加使用次数, 但延长冷却不会减少使用次数, 且延长冷却不会大于配置的刷新冷却
     */
    public void increaseOrReduceCooldown(long cooldown) {
        if (cooldown == 0) return;
        lock.lock();
        try {
            refresh();
            // 满次数中止操作
            if (amount == config.maxAmount) return;
            // 延长冷却
            if (cooldown > 0) {
                val time = currentTimeMillis();
                // 获取离次数刷新还有多久
                val delta = nextRecoveryTime - time;
                // 重新设置刷新时间
                nextRecoveryTime = time + Math.min(config.cooldown, delta + cooldown);
                // 缩减冷却
            } else {
                // 加速恢复
                nextRecoveryTime += cooldown;
                // 刷新
                refresh();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置最大累计次数
     *
     * @param maxAmount 最大累计次数
     */
    public void setMaxAmount(int maxAmount) {
        maxAmount = Math.max(1, maxAmount);
        lock.lock();
        try {
            val preMaxAmount = config.maxAmount;
            // 无变化则中止操作
            if (preMaxAmount == maxAmount) return;
            config.maxAmount = maxAmount;
            // 更新后数量超了
            if (amount >= maxAmount) {
                // 规范数量
                amount = maxAmount;
                // 重新设置刷新时间
                nextRecoveryTime = currentTimeMillis();
                // 之前是满的, 更新后不满
            } else if (amount >= preMaxAmount) {
                // 重新设置刷新时间
                nextRecoveryTime = currentTimeMillis() + config.cooldown;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置刷新冷却
     *
     * @param cooldown 刷新冷却
     */
    public void setConfigCooldown(long cooldown) {
        cooldown = Math.max(1, cooldown);
        lock.lock();
        try {
            config.cooldown = cooldown;
            // 获取还有多久刷新
            val time = currentTimeMillis();
            val delta = nextRecoveryTime - time;
            // 如果还未刷新且剩余时间大于新设置的最大刷新时间
            if (delta > 0 && delta > cooldown) {
                // 重新设置刷新时间
                nextRecoveryTime = time + cooldown;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 冷却配置
     */
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    public static class Config {
        /**
         * 初始次数
         */
        private final int initialAmount;
        /**
         * 最大次数
         */
        private int maxAmount;
        /**
         * 冷却时间
         */
        private long cooldown;

        public Config(int maxAmount, long cooldown) {
            this(maxAmount, maxAmount, cooldown);
        }

        public Config(int initialAmount, int maxAmount, long cooldown) {
            this.maxAmount = Math.max(1, maxAmount);
            this.initialAmount = Math.max(0, Math.min(initialAmount, this.maxAmount));
            this.cooldown = Math.max(1, cooldown);
        }
    }

    /**
     * 当前冷却状态
     */
    @Getter
    @AllArgsConstructor
    public static class Status {
        /**
         * 当前可用次数
         */
        private final int amount;
        /**
         * 下次刷新时间
         */
        private final long nextRecoveryTime;

        /**
         * @return 距下次刷新还有多久
         */
        public long getRemainingRecoveryTime() {
            return Math.max(0, nextRecoveryTime - currentTimeMillis());
        }
    }

    /**
     * 使用结果
     */
    @Getter
    @AllArgsConstructor
    public static class ConsumeResult {
        /**
         * 是否使用成功
         */
        private final boolean success;
        /**
         * 当前冷却状态
         */
        private final @NonNull StackableCooldown.Status status;
    }
}
