package cz.cuni.lf1.thunderstorm.parser.thresholding

import cz.cuni.lf1.thunderstorm.algorithms.filters.FilterWithVariables
import cz.cuni.lf1.thunderstorm.parser.FormulaParserException
import cz.cuni.lf1.thunderstorm.parser.SymbolTable
import cz.cuni.lf1.thunderstorm.parser.syntaxtree.*
import java.lang.String.join

public class ThresholdingSymbolTable(private val declaredVariables: Map<String, RefreshableVariable<FilterWithVariables>>)
    : SymbolTable {

    override fun getFunctionValue(name: String, param: RetVal)
            = when (name) {
                "var" -> param.variance()
                "std" -> param.std()
                "mean" -> param.mean()
                "median" -> param.median()
                "max" -> param.max()
                "min" -> param.min()
                "sum" -> param.sum()
                "abs" -> param.abs()
                else -> throw FormulaParserException("Semantic error! Function '$name' does not exist!");
            }

    override fun getVariableValue(varName: String, namespace: String?): RetVal {
        try {
            val filter = declaredVariables[namespace ?: ""]!!.getValue()
            val value = filter.variables[varName]!!.getValue()!!
            return RetVal(value)
        } catch (ex: Exception) {
            val name = join(".", arrayOf(namespace, varName).filterNotNull())
            throw FormulaParserException("Semantic error! Variable '$name' has no set value!", ex)
        }
    }
}