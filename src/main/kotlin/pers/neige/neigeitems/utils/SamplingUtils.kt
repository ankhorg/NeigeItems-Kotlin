package pers.neige.neigeitems.utils

import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ln
import kotlin.math.pow

/**
 * 随机采样相关工具类
 */
object SamplingUtils {
    @JvmStatic
    fun <T> aExpj(samples: Map<T, Double>, amount: Int): List<T> {
        // 小于等于0直接返回空列表
        if (amount <= 0) {
            return arrayListOf()
        }
        // 等于长度直接返回全集
        if (amount == samples.size) {
            return samples.keys.toList()
        }
        val heap = PriorityQueue<Pair<Double, T>>(compareBy { it.first })
        var thresholdX: Double? = null
        var thresholdT = 0.0
        var weightAcc = 0.0

        for ((item, weight) in samples) {
            if (heap.size < amount) {
                val randNum = ThreadLocalRandom.current().nextDouble(0.0, 1.0)
                val ki = randNum.pow(1.0 / weight)
                heap.add(ki to item)
                continue
            }

            if (weightAcc == 0.0) {
                thresholdT = heap.peek().first
                val randNum = ThreadLocalRandom.current().nextDouble(0.0, 1.0)
                thresholdX = ln(randNum) / ln(thresholdT)
            }

            if (weightAcc + weight < thresholdX!!) {
                weightAcc += weight
                continue
            } else {
                weightAcc = 0.0
            }

            val tW = thresholdT.pow(weight)
            val randNum = ThreadLocalRandom.current().nextDouble(tW, 1.0)
            val ki = randNum.pow(1.0 / weight)
            heap.poll()
            heap.add(ki to item)
        }

        return heap.map { it.second }
    }

    @JvmStatic
    fun <T> aExpj(samples: List<Pair<T, Double>>, amount: Int): List<T> {
        // 小于等于0直接返回空列表
        if (amount <= 0) {
            return arrayListOf()
        }
        // 等于长度直接返回全集
        if (amount == samples.size) {
            return samples.map { it.first }
        }
        val heap = PriorityQueue<Pair<Double, T>>(compareBy { it.first })
        var thresholdX: Double? = null
        var thresholdT = 0.0
        var weightAcc = 0.0

        for ((item, weight) in samples) {
            if (heap.size < amount) {
                val randNum = ThreadLocalRandom.current().nextDouble(0.0, 1.0)
                val ki = randNum.pow(1.0 / weight)
                heap.add(ki to item)
                continue
            }

            if (weightAcc == 0.0) {
                thresholdT = heap.peek().first
                val randNum = ThreadLocalRandom.current().nextDouble(0.0, 1.0)
                thresholdX = ln(randNum) / ln(thresholdT)
            }

            if (weightAcc + weight < thresholdX!!) {
                weightAcc += weight
                continue
            } else {
                weightAcc = 0.0
            }

            val tW = thresholdT.pow(weight)
            val randNum = ThreadLocalRandom.current().nextDouble(tW, 1.0)
            val ki = randNum.pow(1.0 / weight)
            heap.poll()
            heap.add(ki to item)
        }

        return heap.map { it.second }
    }

    @JvmStatic
    fun <T> weight(
        info: Map<T, Double>
    ): T? {
        var total = 0.0
        for (value in info.values) {
            total += value
        }
        return weight(info, total)
    }

    @JvmStatic
    fun <T> weight(
        info: Map<T, Double>,
        total: Double
    ): T? {
        return when {
            info.isEmpty() -> null
            else -> {
                val random = ThreadLocalRandom.current().nextDouble() * total
                var current = 0.0
                var result: T? = null
                for ((key, value) in info) {
                    current += value
                    if (random <= current) {
                        result = key
                        break
                    }
                }
                result
            }
        }
    }

    @JvmStatic
    fun <T> weightBigDecimal(
        info: Map<T, BigDecimal>
    ): T? {
        var total = BigDecimal.ZERO
        for (value in info.values) {
            total = total.add(value)
        }
        return weightBigDecimal(info, total)
    }

    @JvmStatic
    fun <T> weightBigDecimal(
        info: Map<T, BigDecimal>,
        total: BigDecimal
    ): T? {
        return when {
            info.isEmpty() -> null
            else -> {
                val random = BigDecimal(ThreadLocalRandom.current().nextDouble().toString()).multiply(total)
                var current = BigDecimal.ZERO
                var result: T? = null
                for ((key, value) in info) {
                    current = current.add(value)
                    if (random <= current) {
                        result = key
                        break
                    }
                }
                result
            }
        }
    }
}