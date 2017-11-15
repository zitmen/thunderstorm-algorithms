package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.SymbolTable

internal class Constant(value: Double) : Node {

    private val value: RetVal = RetVal(value)

    public override fun eval(symbols: SymbolTable) = value
}
