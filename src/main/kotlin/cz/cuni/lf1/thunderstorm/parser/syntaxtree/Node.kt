package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.FormulaParserException

internal interface Node {

    public fun eval(variables: Map<String, () -> RetVal>): RetVal

    @Throws(FormulaParserException::class)
    public fun semanticScan(variables: Set<String>): Unit
}
