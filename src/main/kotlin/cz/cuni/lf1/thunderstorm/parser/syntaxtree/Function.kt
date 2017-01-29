package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.FormulaParserException

internal class Function(private val fnName: String, private val argument: Node) : Node {

    private val builtInFunctions = hashSetOf("var", "std", "mean", "median", "max", "min", "sum", "abs")

    @Throws(FormulaParserException::class)
    public override fun semanticScan(variables: Set<String>) {
        if(!builtInFunctions.contains(fnName.toLowerCase()))
            throw FormulaParserException("Semantic error! Function '$fnName' does not exist!");
        argument.semanticScan(variables)
    }

    public override fun eval(variables: Map<String, () -> RetVal>): RetVal {
        val param = argument.eval(variables)
        return when (fnName) {
            "var" -> param.variance()
            "std" -> param.std()
            "mean" -> param.mean()
            "median" -> param.median()
            "max" -> param.max()
            "min" -> param.min()
            "sum" -> param.sum()
            "abs" -> param.abs()
            else -> throw FormulaParserException("Semantic error! Function '$fnName' does not exist!");
        }
    }
}
