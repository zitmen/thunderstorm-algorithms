package cz.cuni.lf1.thunderstorm.parser.thresholding

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.parser.FormulaParser
import cz.cuni.lf1.thunderstorm.parser.FormulaParserException
import cz.cuni.lf1.thunderstorm.parser.syntaxtree.RetVal

public class Thresholder
    @Throws(FormulaParserException::class)
    constructor(formula: String, private val symbolTable: Map<String, (GrayScaleImage) -> GrayScaleImage>) {

    private val tree = FormulaParser(formula).parse()

    init {
        tree.semanticScan(symbolTable.keys)
    }

    @Throws(FormulaParserException::class)
    public fun evaluate(img: GrayScaleImage)
        = tree.eval(symbolTable.map { kv -> Pair(kv.key, { RetVal(kv.value(img)) }) }.toMap()).getValue()
}