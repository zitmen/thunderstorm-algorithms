package cz.cuni.lf1.thunderstorm.parser.syntaxtree

internal enum class Operator {
    ADD, SUB, MUL, DIV, MOD, POW,   // arithmetic
    AND, OR, LT, GT, EQ, NEQ        // logic
}