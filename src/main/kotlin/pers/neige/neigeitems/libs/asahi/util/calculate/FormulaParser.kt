package pers.neige.neigeitems.libs.asahi.util.calculate

@Deprecated("replaced with pers.neige.neigeitems.calculate.FormulaParser")
object FormulaParser {
    @JvmStatic
    @Deprecated(
        "replaced with pers.neige.neigeitems.calculate.FormulaParser.calculate", ReplaceWith(
            "pers.neige.neigeitems.calculate.FormulaParser.calculate(this)",
            "pers",
            "pers.neige.neigeitems.libs.asahi.util.calculate.FormulaParser.calculate"
        )
    )
    fun String.calculate(): Double {
        return pers.neige.neigeitems.calculate.FormulaParser.calculate(this)
    }
}
