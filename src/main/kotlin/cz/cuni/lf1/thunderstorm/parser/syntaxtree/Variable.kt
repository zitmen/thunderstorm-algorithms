package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.FormulaParserException

internal class Variable(private val varName: String) : Node {

    @Throws(FormulaParserException::class)
    public override fun semanticScan(variables: Set<String>) {
        if (varName !in variables) {
            throw FormulaParserException("Variable '$varName' does not exist!");
        }
    }

    public override fun eval(variables: Map<String, () -> RetVal>): RetVal {
        return variables[varName]?.invoke()
            ?: throw FormulaParserException("Variable '$varName' can't be evaluated!");
    }
}
