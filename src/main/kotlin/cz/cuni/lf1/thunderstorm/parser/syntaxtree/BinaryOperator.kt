package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.FormulaParserException

internal class BinaryOperator(private val operator: Operator, private val leftOperand: Node, private val rightOperand: Node) : Node {

    public override fun eval(variables: Map<String, () -> RetVal>): RetVal {
        return when (operator) {
            Operator.ADD -> leftOperand.eval(variables).plus (rightOperand.eval(variables))
            Operator.SUB -> leftOperand.eval(variables).minus(rightOperand.eval(variables))
            Operator.MUL -> leftOperand.eval(variables).times(rightOperand.eval(variables))
            Operator.DIV -> leftOperand.eval(variables).div  (rightOperand.eval(variables))
            Operator.MOD -> leftOperand.eval(variables).mod  (rightOperand.eval(variables))
            Operator.POW -> leftOperand.eval(variables).pow  (rightOperand.eval(variables))
            Operator.AND -> leftOperand.eval(variables).and  (rightOperand.eval(variables))
            Operator.OR  -> leftOperand.eval(variables).or   (rightOperand.eval(variables))
            Operator.LT  -> leftOperand.eval(variables).lt   (rightOperand.eval(variables))
            Operator.GT  -> leftOperand.eval(variables).gt   (rightOperand.eval(variables))
            Operator.EQ  -> leftOperand.eval(variables).eq   (rightOperand.eval(variables))
            Operator.NEQ -> leftOperand.eval(variables).neq  (rightOperand.eval(variables))
            else -> throw UnsupportedOperationException("Unknown operator! Only supported operators are: + - * / ^ & | < > = !=")
        }
    }

    @Throws(FormulaParserException::class)
    public override fun semanticScan(variables: Set<String>) {
        leftOperand.semanticScan(variables)
        rightOperand.semanticScan(variables)
    }
}