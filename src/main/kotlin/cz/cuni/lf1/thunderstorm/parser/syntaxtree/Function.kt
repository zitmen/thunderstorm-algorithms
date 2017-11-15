package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.SymbolTable

internal class Function(private val fnName: String, private val argument: Node) : Node {

    public override fun eval(symbols: SymbolTable)
        = symbols.getFunctionValue(fnName, argument.eval(symbols))
}
