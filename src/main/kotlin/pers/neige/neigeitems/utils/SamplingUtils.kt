package pers.neige.neigeitems.utils

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ln
import kotlin.math.pow

/**
 * 随机采样相关工具类
 */
object SamplingUtils {
    @JvmStatic
    fun <T> aExpj(samples: HashMap<T, Double>, m: Int): List<T> {
        val heap = PriorityQueue<Pair<Double, T>>(compareBy { it.first })
        var thresholdX: Double? = null
        var thresholdT = 0.0
        var weightAcc = 0.0

        for ((item, weight) in samples) {
            if (heap.size < m) {
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
}