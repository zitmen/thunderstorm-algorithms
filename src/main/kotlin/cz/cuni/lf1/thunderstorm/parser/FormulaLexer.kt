package cz.cuni.lf1.thunderstorm.parser

import java.util.*

internal class FormulaLexer(formula: String) {

    private val scanner: Scanner = Scanner(formula.trim())

    init {
        scanner.useDelimiter("")
    }

    public fun nextToken(): FormulaToken {
        if(!scanner.hasNext()) return FormulaToken.EOI
        scanner.skip("[ ]*")

        if(scanner.hasNextFloat()) {
            val token = FormulaToken.FLOAT
            // note: don't know why scanner.nextFloat does not work??!! using regexp instead:
            //http://stackoverflow.com/questions/2293780/how-to-detect-a-floating-point-number-using-a-regular-expression
            token.token = scanner.findInLine("(([0-9]+\\.?[0-9]*)|(\\.[0-9]+))([Ee][+-]?[0-9]+)?")
            return token
        } else if(scanner.hasNext("[#_a-zA-Z]")) {
            val token = FormulaToken.NAME
            token.token = scanner.findInLine("[#_a-zA-Z0-9]+")
            return token
        } else {
            val strToken = scanner.next()    // --> nextCharacter()
            val token = when (strToken[0]) {
                '+' -> FormulaToken.OP_ADD
                '-' -> FormulaToken.OP_SUB
                '*' -> FormulaToken.OP_MUL
                '/' -> FormulaToken.OP_DIV
                '%' -> FormulaToken.OP_MOD
                '^' -> FormulaToken.OP_POW
                '&' -> FormulaToken.OP_AND
                '|' -> FormulaToken.OP_OR
                '<' -> FormulaToken.OP_LT
                '>' -> FormulaToken.OP_GT
                '=' -> FormulaToken.OP_EQ
                '!' -> FormulaToken.OP_NOT
                '(' -> FormulaToken.LPAR
                ')' -> FormulaToken.RPAR
                '.' -> FormulaToken.DOT
                else -> FormulaToken.UNKNOWN
            }
            token.token = strToken
            return token
        }
    }
}
