package pers.neige.neigeitems.utils

import java.util.concurrent.ThreadLocalRandom

/**
 * 随机采样相关工具类
 */
object SamplingUtils {
    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param amount 采样数量
     * @return 采样结果
     */
    @Deprecated(
        "啥也不是, 折腾半天比无脑整还慢",
        ReplaceWith("weight(samples, amount)", "pers.neige.neigeitems.utils.SamplingUtils.weight")
    )
    @JvmStatic
    fun <T> aExpj(samples: Map<T, Double>, amount: Int): List<T> {
        return weight(samples, amount)
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param amount 采样数量
     * @return 采样结果
     */
    @Deprecated(
        "啥也不是, 折腾半天比无脑整还慢",
        ReplaceWith("weight(samples, amount)", "pers.neige.neigeitems.utils.SamplingUtils.weight")
    )
    @JvmStatic
    fun <T> aExpj(samples: List<Pair<T, Double>>, amount: Int): List<T> {
        return weight(samples, amount)
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param amount 采样数量
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weight(
        samples: Map<T, Double>,
        amount: Int
    ): List<T> {
        var total = 0.0
        val temp = HashMap<T, Double>()
        for ((key, value) in samples) {
            total += value
            temp[key] = value
        }
        val result = ArrayList<T>()
        repeat(amount) {
            val current = weight(temp, total) ?: return@repeat
            result.add(current)
            total -= temp.remove(current)!!
        }
        return result
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param amount 采样数量
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weight(
        samples: List<Pair<T, Double>>,
        amount: Int
    ): List<T> {
        var total = 0.0
        val temp: MutableList<Pair<T, Double>> = ArrayList()
        for (pair in samples) {
            total += pair.second
            temp.add(pair)
        }
        val result = ArrayList<T>()
        repeat(amount) {
            val current = weightWithIndex(temp, total) ?: return@repeat
            result.add(current.value)
            temp.removeAt(current.index)
            total -= current.weight
        }
        return result
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param amount 采样数量
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weightWithIndex(
        samples: List<Pair<T, Double>>,
        amount: Int
    ): List<SamplingResult<T>> {
        var total = 0.0
        val temp: MutableList<Pair<T, Double>> = ArrayList()
        for (pair in samples) {
            total += pair.second
            temp.add(pair)
        }
        val result = ArrayList<SamplingResult<T>>()
        repeat(amount) {
            val current = weightWithIndex(temp, total) ?: return@repeat
            result.add(current)
            temp.removeAt(current.index)
            total -= current.weight
        }
        return result
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weight(
        samples: Map<T, Double>
    ): T? {
        var total = 0.0
        for (value in samples.values) {
            total += value
        }
        return weight(samples, total)
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param total 权重和
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weight(
        samples: Map<T, Double>,
        total: Double
    ): T? {
        if (samples.isEmpty()) return null
        val random = ThreadLocalRandom.current().nextDouble() * total
        var current = 0.0
        for ((key, value) in samples) {
            current += value
            if (random <= current) {
                return key
            }
        }
        return null
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weight(
        samples: List<Pair<T, Double>>
    ): T? {
        var total = 0.0
        for (value in samples) {
            total += value.second
        }
        return weight(samples, total)
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param total 权重和
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weight(
        samples: List<Pair<T, Double>>,
        total: Double
    ): T? {
        if (samples.isEmpty()) return null
        val random = ThreadLocalRandom.current().nextDouble() * total
        var current = 0.0
        samples.forEachIndexed { index, pair ->
            current += pair.second
            if (random <= current) {
                return SamplingResult(pair.first, pair.second, index).value
            }
        }
        return null
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weightWithIndex(
        samples: List<Pair<T, Double>>
    ): SamplingResult<T>? {
        var total = 0.0
        for (value in samples) {
            total += value.second
        }
        return weightWithIndex(samples, total)
    }

    /**
     * 加权随机采样.
     *
     * @param samples 样本集
     * @param total 权重和
     * @return 采样结果
     */
    @JvmStatic
    fun <T> weightWithIndex(
        samples: List<Pair<T, Double>>,
        total: Double
    ): SamplingResult<T>? {
        if (samples.isEmpty()) return null
        val random = ThreadLocalRandom.current().nextDouble() * total
        var current = 0.0
        samples.forEachIndexed { index, pair ->
            current += pair.second
            if (random <= current) {
                return SamplingResult(pair.first, pair.second, index)
            }
        }
        return null
    }

    class SamplingResult<T>(val value: T, val weight: Double, val index: Int)
}