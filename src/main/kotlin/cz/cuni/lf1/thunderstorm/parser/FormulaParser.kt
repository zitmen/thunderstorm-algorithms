package cz.cuni.lf1.thunderstorm.parser

import cz.cuni.lf1.thunderstorm.parser.syntaxtree.*

internal class FormulaParser(formula: String) {

    private val lexer = FormulaLexer(formula)
    private var token: FormulaToken? = null

    private fun peek(): FormulaToken {
        if(token == null) token = lexer.nextToken()
        return token!!
    }

    @Throws(FormulaParserException::class)
    private fun match(type: FormulaToken): String {
        if(peek() == type) {
            val tok = token!!.token
            token = null
            return tok!!
        }
        error("Syntax error near `${token!!.token}`. Expected `$type` instead!")
    }

    @Throws(FormulaParserException::class)
    public fun parse(): Node {
        return expr()
    }

    /* ----------------------------------------------
     * --- Implementation of LL1 recursive parser ---
     * ---------------------------------------------- */
    @Throws(FormulaParserException::class)
    private fun error(message: String?): Nothing {
        if((message == null) || message.trim().isEmpty()) {
            throw FormulaParserException("Syntax error!")
        }
        throw FormulaParserException(message)
    }

    @Throws(FormulaParserException::class)
    private fun expr(): Node {
        return logOrExpr()
    }

    @Throws(FormulaParserException::class)
    private fun logOrExpr(): Node {    // l|r
        return logOrExprTail(logAndExpr())
    }

    @Throws(FormulaParserException::class)
    private fun logOrExprTail(l: Node): Node {    // l|r
        when (peek()) {
            FormulaToken.OP_OR -> {
                match(FormulaToken.OP_OR)
                return logOrExprTail(BinaryOperator(Operator.OR, l, logAndExpr()))
            }
            else -> {
                return l
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun logAndExpr(): Node {    // l&r
        return logAndExprTail(relExpr())
    }

    @Throws(FormulaParserException::class)
    private fun logAndExprTail(l: Node): Node {    // l&r
        when (peek()) {
            FormulaToken.OP_AND -> {
                match(FormulaToken.OP_AND)
                return logAndExprTail(BinaryOperator(Operator.AND, l, relExpr()))
            }
            else -> {
                return l
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun relExpr(): Node {    // l=r, l!=r, l<r, l>r
        return relExprTail(addSubExpr())
    }

    @Throws(FormulaParserException::class)
    private fun relExprTail(l: Node): Node {    // l=r, l<r, l>r
        when (peek()) {
            FormulaToken.OP_NOT -> {
                match(FormulaToken.OP_NOT)
                match(FormulaToken.OP_EQ)
                return relExprTail(BinaryOperator(Operator.NEQ, l, addSubExpr()))
            }
            FormulaToken.OP_EQ -> {
                match(FormulaToken.OP_EQ)
                return relExprTail(BinaryOperator(Operator.EQ, l, addSubExpr()))
            }
            FormulaToken.OP_GT -> {
                match(FormulaToken.OP_GT)
                return relExprTail(BinaryOperator(Operator.GT, l, addSubExpr()))
            }
            FormulaToken.OP_LT -> {
                match(FormulaToken.OP_LT)
                return relExprTail(BinaryOperator(Operator.LT, l, addSubExpr()))
            }
            else -> {
                return l
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun addSubExpr(): Node {    // l+r, l-r
        return addSubExprTail(mulDivExpr())
    }

    @Throws(FormulaParserException::class)
    private fun addSubExprTail(l: Node): Node {    // l+r, l-r
        when (peek()) {
            FormulaToken.OP_ADD -> {
                match(FormulaToken.OP_ADD)
                return addSubExprTail(BinaryOperator(Operator.ADD, l, mulDivExpr()))
            }
            FormulaToken.OP_SUB -> {
                match(FormulaToken.OP_SUB)
                return addSubExprTail(BinaryOperator(Operator.SUB, l, mulDivExpr()))
            }
            else -> {
                return l
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun mulDivExpr(): Node {    // l*r, l/r, l%r
        return mulDivExprTail(powExpr())
    }

    @Throws(FormulaParserException::class)
    private fun mulDivExprTail(l: Node): Node {    // l*r, l/r, l%r
        when (peek()) {
            FormulaToken.OP_MUL -> {
                match(FormulaToken.OP_MUL)
                return mulDivExprTail(BinaryOperator(Operator.MUL, l, powExpr()))
            }
            FormulaToken.OP_DIV -> {
                match(FormulaToken.OP_DIV)
                return mulDivExprTail(BinaryOperator(Operator.DIV, l, powExpr()))
            }
            FormulaToken.OP_MOD -> {
                match(FormulaToken.OP_MOD)
                return mulDivExprTail(BinaryOperator(Operator.MOD, l, powExpr()))
            }
            else -> {
                return l
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun powExpr(): Node {   // x^n
        return powExprTail(unaryExpr())
    }

    @Throws(FormulaParserException::class)
    private fun powExprTail(l: Node): Node {   // x^n
        when (peek()) {
            FormulaToken.OP_POW -> {
                match(FormulaToken.OP_POW)
                return powExprTail(BinaryOperator(Operator.POW, l, unaryExpr()))
            }
            else -> {
                return l
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun unaryExpr(): Node {   // -x or +x
        when (peek()) {
            FormulaToken.OP_ADD -> {
                match(FormulaToken.OP_ADD)
                return BinaryOperator(Operator.ADD, Constant(0.0), atom())
            }
            FormulaToken.OP_SUB -> {
                match(FormulaToken.OP_SUB)
                return BinaryOperator(Operator.SUB, Constant(0.0), atom())
            }
            else -> {
                return atom()
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun atom(): Node {
        when (peek()) {
            FormulaToken.LPAR -> {
                match(FormulaToken.LPAR)
                val e = expr()
                match(FormulaToken.RPAR)
                return e
            }
            FormulaToken.FLOAT -> {
                return floatVal()
            }
            FormulaToken.NAME -> {
                return name()
            }
            else -> {
                error("Syntax error near `${token!!.token}`. Expected `(expression)` or a number or a variable instead!")
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun name(): Node {
        val tok = match(FormulaToken.NAME)
        when (peek()) {
            FormulaToken.DOT -> {
                match(FormulaToken.DOT)
                return Variable(namespace = tok, varName = match(FormulaToken.NAME))
            }
            FormulaToken.LPAR -> {
                match(FormulaToken.LPAR)
                val arg = expr()
                match(FormulaToken.RPAR)
                return Function(tok, arg) // function call
            }
            else -> {
                return Variable(varName = tok)   // just a variable (no object)
            }
        }
    }

    @Throws(FormulaParserException::class)
    private fun floatVal(): Node {
        return Constant(match(FormulaToken.FLOAT).toDouble())
    }
}