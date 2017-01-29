package cz.cuni.lf1.thunderstorm.parser

internal enum class FormulaToken(public val type: Int) {

    UNKNOWN(-1),
    EOI(0),    // End Of Input
    OP_ADD(1),
    OP_SUB(2),
    OP_MUL(3),
    OP_DIV(4),
    OP_MOD(5),
    OP_POW(6),
    OP_AND(7),
    OP_OR(8),
    OP_LT(9),
    OP_GT(10),
    OP_EQ(11),
    OP_NOT(12),
    LPAR(13),
    RPAR(14),
    DOT(15),
    NAME(16),
    FLOAT(17);

    public var token: String? = null

    public override fun toString(): String {
        if (type == UNKNOWN.type) return "unknown"
        return TYPE_NAMES[type]
    }

    companion object {
        private val TYPE_NAMES = arrayOf(
                "end of input", "+", "-", "*", "/", "%", "^", "&", "|", "<", ">",
                "=", "!", "(", ")", ".", "a variable", "a number")
    }
}
