package pers.neige.neigeitems.manager

import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.section.impl.StringsParser

object SectionManager {
    val sectionParsers = HashMap<String, SectionParser>()
    init {
        sectionParsers["strings"] = StringsParser
    }
}