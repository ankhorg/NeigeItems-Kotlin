package pers.neige.neigeitems.hook.placeholderapi.impl

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker
import pers.neige.neigeitems.hook.placeholderapi.PlaceholderExpansion
import java.util.*

/**
 * 低版本PlaceholderAPI挂钩
 *
 * @constructor 启用低版本PlaceholderAPI挂钩
 */
class LegacyPapiHookerImpl : PapiHooker() {
    private val placeholders get() = PlaceholderAPI.getPlaceholders()

    init {
        placeholders
    }

    override fun papi(player: OfflinePlayer?, text: String): String {
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

            val placeholder = placeholders[lowercaseIdentifierString]
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

    override fun toSection(text: String, onlyValid: Boolean): String {
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

            if (onlyValid) {
                val placeholder = placeholders[lowercaseIdentifierString]
                if (placeholder == null) {
                    builder.append('%').append(identifierString)

                    if (identified) {
                        builder.append('_')
                    }

                    builder.append(parametersString).append('%')
                    i++
                    continue
                }
            }

            val replacement = "<papi::${identifierString}_$parametersString>"

            builder.append(replacement)
            i++
        }

        return builder.toString()
    }

    override fun hasPapi(text: String): Boolean {
        val chars = text.toCharArray()

        val identifier = StringBuilder()
        val parameters = StringBuilder()

        var i = 0
        while (i < chars.size) {
            val l = chars[i]

            if ((l != '%') || ((i + 1) >= chars.size)) {
                i++
                continue
            }

            var identified = false
            var invalid = true

            while (++i < chars.size) {
                val p = chars[i]

                if (p == ' ' && !identified) {
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

            identifier.setLength(0)
            parameters.setLength(0)

            if (invalid) {
                i++
                continue
            }

            val placeholder = placeholders[lowercaseIdentifierString]
            if (placeholder == null) {
                i++
                continue
            } else {
                return true
            }
        }

        return false
    }

    override fun request(player: OfflinePlayer, identifier: String, parameters: String): String {
        val placeholder = placeholders[identifier.lowercase(Locale.getDefault())]
        if (placeholder != null) {
            val replacement = placeholder.onRequest(player, parameters)
            if (replacement != null) {
                return replacement
            }
        }
        return "%${identifier}_$parameters%"
    }

    override fun unregisterExpansion(expansion: PlaceholderExpansion) {
        PlaceholderAPI.unregisterPlaceholderHook(expansion.expansion.identifier)
    }
}