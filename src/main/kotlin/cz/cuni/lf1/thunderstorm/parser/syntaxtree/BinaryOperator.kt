package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.parser.SymbolTable

internal class BinaryOperator(private val operator: Operator, private val leftOperand: Node, private val rightOperand: Node) : Node {

    public override fun eval(symbols: SymbolTable)
        = when (operator) {
            Operator.ADD -> leftOperand.eval(symbols).plus (rightOperand.eval(symbols))
            Operator.SUB -> leftOperand.eval(symbols).minus(rightOperand.eval(symbols))
            Operator.MUL -> leftOperand.eval(symbols).times(rightOperand.eval(symbols))
            Operator.DIV -> leftOperand.eval(symbols).div  (rightOperand.eval(symbols))
            Operator.MOD -> leftOperand.eval(symbols).rem  (rightOperand.eval(symbols))
            Operator.POW -> leftOperand.eval(symbols).pow  (rightOperand.eval(symbols))
            Operator.AND -> leftOperand.eval(symbols).and  (rightOperand.eval(symbols))
            Operator.OR  -> leftOperand.eval(symbols).or   (rightOperand.eval(symbols))
            Operator.LT  -> leftOperand.eval(symbols).lt   (rightOperand.eval(symbols))
            Operator.GT  -> leftOperand.eval(symbols).gt   (rightOperand.eval(symbols))
            Operator.EQ  -> leftOperand.eval(symbols).eq   (rightOperand.eval(symbols))
            Operator.NEQ -> leftOperand.eval(symbols).neq  (rightOperand.eval(symbols))
        }
}