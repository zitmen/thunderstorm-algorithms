package cz.cuni.lf1.thunderstorm.parser.syntaxtree

internal class Constant(value: Double) : Node {

    private val value: RetVal = RetVal(value)

    public override fun eval(variables: Map<String, () -> RetVal>): RetVal = value
    public override fun semanticScan(variables: Set<String>): Unit { }
}
