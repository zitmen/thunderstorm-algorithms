package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.SymbolTable

internal interface Node {

    public fun eval(symbols: SymbolTable): RetVal
}
