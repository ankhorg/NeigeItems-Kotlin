package pers.neige.neigeitems.hook.placeholderapi.impl

import me.clip.placeholderapi.PlaceholderAPIPlugin
import org.bukkit.OfflinePlayer
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker
import pers.neige.neigeitems.hook.placeholderapi.PlaceholderExpansion
import java.util.*

/**
 * 高版本PlaceholderAPI挂钩
 *
 * @constructor 启用高版本PlaceholderAPI挂钩
 */
class PapiHookerImpl : PapiHooker() {
    private val localExpansionManager get() = PlaceholderAPIPlugin.getInstance().localExpansionManager

    init {
        localExpansionManager
    }

    override fun papi(player: OfflinePlayer, text: String): String {
        val chars = text.toCharArray()
        val builder = StringBuilder(text.length)

        val identifier = StringBuilder()
        val parameters = StringBuilder()

        var i = 0
        while (i < chars.size) {
            val l = chars[i]

            if ((l != '%') || ((i + 1) >= chars.size)) {
                builder.append(l)
                i++
                continue
            }

            var identified = false
            var invalid = true
            var hadSpace = false

            while (++i < chars.size) {
                val p = chars[i]

                if (p == ' ' && !identified) {
                    hadSpace = true
                    break
                }
                if (p == '%') {
                    invalid = false
                    break
                }

                if (p == '_' && !identified) {
                    identified = true
                    continue
                }

                if (identified) {
                    parameters.append(p)
                } else {
                    identifier.append(p)
                }
            }

            val identifierString = identifier.toString()
            val lowercaseIdentifierString = identifierString.lowercase(Locale.getDefault())
            val parametersString = parameters.toString()

            identifier.setLength(0)
            parameters.setLength(0)

            if (invalid) {
                builder.append('%').append(identifierString)

                if (identified) {
                    builder.append('_').append(parametersString)
                }

                if (hadSpace) {
                    builder.append(' ')
                }
                i++
                continue
            }

            val placeholder = localExpansionManager.getExpansion(lowercaseIdentifierString)
            if (placeholder == null) {
                builder.append('%').append(identifierString)

                if (identified) {
                    builder.append('_')
                }

                builder.append(parametersString).append('%')
                i++
                continue
            }

            val replacement = placeholder.onRequest(player, parametersString)
            if (replacement == null) {
                builder.append('%').append(identifierString)

                if (identified) {
                    builder.append('_')
                }

                builder.append(parametersString).append('%')
                i++
                continue
            }

            builder.append(replacement)
            i++
        }

        return builder.toString()
    }

    override fun request(player: OfflinePlayer, identifier: String, parameters: String): String {
        val placeholder = localExpansionManager.getExpansion(identifier.lowercase(Locale.getDefault()))
        if (placeholder != null) {
            val replacement = placeholder.onRequest(player, parameters)
            if (replacement != null) {
                return replacement
            }
        }
        return "%${identifier}_$parameters%"
    }

    override fun unregisterExpansion(expansion: PlaceholderExpansion) {
        expansion.expansion.unregister()
    }
}