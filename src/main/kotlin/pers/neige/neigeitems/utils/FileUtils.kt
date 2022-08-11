package pers.neige.neigeitems.utils

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream


/**
 * 文件相关工具类
 */
object FileUtils {
    /**
     * 解析文件编码
     *
     * @param file 待解析文件
     * @return 编码类型
     */
    @JvmStatic
    fun charset(file: File): String {
        var charset = "GBK"
        val first3Bytes = ByteArray(3)
        try {
            var checked = false
            val bis = BufferedInputStream(FileInputStream(file))
            bis.mark(0)
            var read = bis.read(first3Bytes, 0, 3)
            if (read == -1) {
                bis.close()
                return charset
            } else if (first3Bytes[0] == 0xFF.toByte() && first3Bytes[1] == 0xFE.toByte()) {
                charset = "UTF-16LE"
                checked = true
            } else if (first3Bytes[0] == 0xFE.toByte() && first3Bytes[1] == 0xFF.toByte()) {
                charset = "UTF-16BE"
                checked = true
            } else if (first3Bytes[0] == 0xEF.toByte() && first3Bytes[1] == 0xBB.toByte() && first3Bytes[2] == 0xBF.toByte()) {
                charset = "UTF-8"
                checked = true
            }
            bis.reset()
            if (!checked) {
                while (bis.read().also { read = it } != -1) {
                    if (read >= 0xF0) break
                    if (read in 0x80..0xBF)
                        break
                    if (read in 0xC0..0xDF) {
                        read = bis.read()
                        if (read in 0x80..0xBF)
                            continue else break
                    } else if (read in 0xE0..0xEF) {
                        read = bis.read()
                        if (read in 0x80..0xBF) {
                            read = bis.read()
                            if (read in 0x80..0xBF) {
                                charset = "UTF-8"
                                break
                            } else break
                        } else break
                    }
                }
            }
            bis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return charset
    }
}