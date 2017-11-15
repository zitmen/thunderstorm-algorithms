package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.SymbolTable

internal class Variable(private val varName: String, private val namespace: String? = null) : Node {

    public override fun eval(symbols: SymbolTable)
        = symbols.getVariableValue(varName, namespace)
}
