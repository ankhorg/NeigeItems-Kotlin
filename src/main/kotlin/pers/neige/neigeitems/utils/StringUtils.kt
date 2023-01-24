package pers.neige.neigeitems.utils

object StringUtils {
    @JvmStatic
    fun List<String>.joinToString(separator: CharSequence = ", ", start: Int = 0): String {
        val buffer = StringBuilder()
        for (count in start until this.size) {
            if (count > start) buffer.append(separator)
            buffer.append(this[count])
        }
        return buffer.toString()
    }
}