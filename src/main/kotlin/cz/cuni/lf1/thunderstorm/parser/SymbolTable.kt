package cz.cuni.lf1.thunderstorm.parser

import cz.cuni.lf1.thunderstorm.parser.syntaxtree.RetVal

public interface SymbolTable {

    public fun getVariableValue(varName: String, namespace: String?): RetVal
    public fun getFunctionValue(name: String, param: RetVal): RetVal
}