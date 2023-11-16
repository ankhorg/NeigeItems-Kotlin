package pers.neige.neigeitems.utils

import java.math.BigDecimal

object StringUtils {
    /**
     * List转文本
     *
     * @param separator 分隔符
     * @param start 起始索引
     * @return 结果文本
     */
    @JvmStatic
    fun List<String>.joinToString(separator: CharSequence = ", ", start: Int = 0): String {
        val buffer = StringBuilder()
        for (count in start until this.size) {
            if (count > start) buffer.append(separator)
            buffer.append(this[count])
        }
        return buffer.toString()
    }

    /**
     * 对文本进行分割
     *
     * @param separator 分隔符
     * @param escape 转义符
     * @return 解析值
     */
    @JvmStatic
    fun String.split(separator: Char, escape: Char): ArrayList<String> {
        // 参数文本
        val chars = this.toCharArray()
        // 所有参数
        val args = ArrayList<String>()
        // 缓存当前参数
        val arg = StringBuilder()

        // 表示前一个字符是不是转义符(即反斜杠)
        var lastIsEscape = false

        // 遍历
        for (char in chars) {
            // 当前字符是不是转义符
            val isEscape = char == escape
            // 当前字符是不是分隔符
            val isSeparator = char == separator
            // 如果当前字符是分隔符且上一个字符不是转义符
            if (isSeparator && !lastIsEscape) {
                // 参数怼回去
                args.add(arg.toString())
                arg.setLength(0)
            } else {
                // 如果前一字符为转义符, 理应判断当前字符是否需要转义
                // 需要转义则吞掉转义符, 不需转义则将转义符视作普通反斜杠处理
                if (!isEscape && !isSeparator && lastIsEscape) {
                    arg.append(escape)
                }
                // 如果当前字符不为转义符, 直接填入字符
                if (!isEscape || lastIsEscape) {
                    arg.append(char)
                }
            }
            // 进行转义符记录
            lastIsEscape = isEscape && !lastIsEscape
        }
        // 处理最后一个参数
        // 如果最后一个字符是转义符, 应将其视作普通反斜杠处理
        if (lastIsEscape) {
            args.add("$arg$escape")
        } else {
            args.add(arg.toString())
        }
        return args
    }

    /**
     * 将文本分为两段
     *
     * @param separator 分隔符
     * @return 分割后文本
     */
    @JvmStatic
    fun String.splitOnce(separator: String): Array<String> {
        return when (val index = this.indexOf(separator)) {
            -1 -> arrayOf(this)
            else -> {
                val pre = this.substring(0, index)
                val post = this.substring(index + separator.length)
                arrayOf(pre, post)
            }
        }
    }

    @JvmStatic
    fun String.splitKt(separator: String, limit: Int): List<String> {
        return this.split(separator, limit = limit)
    }

    /**
     * 转Int
     *
     * @param string 分隔符
     * @return Int?
     */
    @JvmStatic
    fun toIntOrNull(string: String): Int? {
        return string.toIntOrNull()
    }

    /**
     * 转Byte
     *
     * @param string 分隔符
     * @return Byte?
     */
    @JvmStatic
    fun toByteOrNull(string: String): Byte? {
        return string.toByteOrNull()
    }

    /**
     * 转Double
     *
     * @param string 分隔符
     * @return Double?
     */
    @JvmStatic
    fun toDoubleOrNull(string: String): Double? {
        return string.toDoubleOrNull()
    }

    /**
     * 转BigDecimal
     *
     * @param string 分隔符
     * @return BigDecimal?
     */
    @JvmStatic
    fun toBigDecimalOrNull(string: String): BigDecimal? {
        return string.toBigDecimalOrNull()
    }
}