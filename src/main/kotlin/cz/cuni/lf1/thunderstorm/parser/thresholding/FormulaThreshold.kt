package cz.cuni.lf1.thunderstorm.parser.thresholding

import cz.cuni.lf1.thunderstorm.algorithms.filters.FilterWithVariables
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.parser.FormulaParser

public class FormulaThreshold(
        formula: String,
        private val variables: Map<String, RefreshableVariable<FilterWithVariables>> = mapOf()) {

    private val tree = FormulaParser(formula).parse()
    private val symbols = ThresholdingSymbolTable(variables)

    public fun getValue(image: GrayScaleImage): Double {
        variables.values.forEach { it.setRefresher { it.apply { filter(image) } } }
        return tree.eval(symbols).getValue()
    }
}