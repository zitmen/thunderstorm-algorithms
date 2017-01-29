package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.datastructures.extensions.*
import cz.cuni.lf1.thunderstorm.parser.FormulaParserException

internal operator fun RetVal.plus(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue() + b.getValue())
        if (b.isVector()) return RetVal(getValue() + b.getVector())
        if (b.isMatrix()) return RetVal(getValue() + b.getMatrix())
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector() + b.getValue())
        if (b.isVector()) return RetVal(getVector() + b.getVector())
        if (b.isMatrix()) throw FormulaParserException("Operation vector+matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix() + b.getValue())
        if (b.isVector()) throw FormulaParserException("Operation matrix+vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix() + b.getMatrix())
    }
    throw FormulaParserException("Operation is not supported!")
}

internal operator fun RetVal.minus(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue() - b.getValue())
        if (b.isVector()) return RetVal(getValue() - b.getVector())
        if (b.isMatrix()) return RetVal(getValue() - b.getMatrix())
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector() - b.getValue())
        if (b.isVector()) return RetVal(getVector() - b.getVector())
        if (b.isMatrix()) throw FormulaParserException("Operation vector-matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix() - b.getValue())
        if (b.isVector()) throw FormulaParserException("Operation matrix-vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix() - b.getMatrix())
    }
    throw FormulaParserException("Operation is not supported!")
}

internal operator fun RetVal.times(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue() * b.getValue())
        if (b.isVector()) return RetVal(getValue() * b.getVector())
        if (b.isMatrix()) return RetVal(getValue() * b.getMatrix())
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector() * b.getValue())
        if (b.isVector()) return RetVal(getVector() * b.getVector())
        if (b.isMatrix()) throw FormulaParserException("Operation vector*matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix() * b.getValue())
        if (b.isVector()) throw FormulaParserException("Operation matrix*vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix() * b.getMatrix())
    }
    throw FormulaParserException("Operation is not supported!")
}

internal operator fun RetVal.div(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue() / b.getValue())
        if (b.isVector()) return RetVal(getValue() / b.getVector())
        if (b.isMatrix()) return RetVal(getValue() / b.getMatrix())
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector() / b.getValue())
        if (b.isVector()) return RetVal(getVector() / b.getVector())
        if (b.isMatrix()) throw FormulaParserException("Operation vector/matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix() / b.getValue())
        if (b.isVector()) throw FormulaParserException("Operation matrix/vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix() / b.getMatrix())
    }
    throw FormulaParserException("Operation is not supported!")
}

internal operator fun RetVal.mod(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue() % b.getValue())
        if (b.isVector()) return RetVal(getValue() % b.getVector())
        if (b.isMatrix()) return RetVal(getValue() % b.getMatrix())
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector() % b.getValue())
        if (b.isVector()) return RetVal(getVector() % b.getVector())
        if (b.isMatrix()) throw FormulaParserException("Operation vector%matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix() % (b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation matrix%vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix() % (b.getMatrix()))
    }
    throw FormulaParserException("Operation is not supported!")
}

internal fun RetVal.pow(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue().pow(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation scalar^vector is not supported!")
        if (b.isMatrix()) throw FormulaParserException("Operation scalar^matrix is not supported!")
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector().pow(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation vector^vector is not supported!")
        if (b.isMatrix()) throw FormulaParserException("Operation vector^matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix().pow(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation matrix^vector is not supported!")
        if (b.isMatrix()) throw FormulaParserException("Operation matrix^matrix is not supported!")
    }
    throw FormulaParserException("Operation is not supported!")
}

internal fun RetVal.and(b: RetVal): RetVal {
    if (isValue() && b.isValue()) return RetVal((getValue().and(b.getValue())))
    if (isVector() && b.isVector()) return RetVal(getVector().and(b.getVector()))
    if (isMatrix() && b.isMatrix()) return RetVal(getMatrix().and(b.getMatrix()))
    throw FormulaParserException("Operator `&` can be used only with variables of the same type, i.e., scalar&scalar, vector&vector, or matrix&matrix!")
}

internal fun RetVal.or(b: RetVal): RetVal {
    if (isValue() && b.isValue()) return RetVal(getValue().or(b.getValue()))
    if (isVector() && b.isVector()) return RetVal(getVector().or(b.getVector()))
    if (isMatrix() && b.isMatrix()) return RetVal(getMatrix().or(b.getMatrix()))
    throw FormulaParserException("Operator `|` can be used only with variables of the same type, i.e., scalar|scalar, vector|vector, or matrix|matrix!")
}

internal fun RetVal.eq(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue().eq(getValue()))
        if (b.isVector()) return RetVal(getValue().eq(b.getVector()))
        if (b.isMatrix()) return RetVal(getValue().eq(b.getMatrix()))
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector().eq(b.getValue()))
        if (b.isVector()) return RetVal(getVector().eq(b.getVector()))
        if (b.isMatrix()) throw FormulaParserException("Operation vector=matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix().eq(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation matrix=vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix().eq(b.getMatrix()))
    }
    throw FormulaParserException("Operation is not supported!")
}

internal fun RetVal.neq(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue().neq(getValue()))
        if (b.isVector()) return RetVal(getValue().neq(b.getVector()))
        if (b.isMatrix()) return RetVal(getValue().neq(b.getMatrix()))
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector().neq(b.getValue()))
        if (b.isVector()) return RetVal(getVector().neq(b.getVector()))
        if (b.isMatrix()) throw FormulaParserException("Operation vector!=matrix is not supported!")
    }
    if (isMatrix()) {
        if (b.isValue()) return RetVal(getMatrix().neq(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation matrix!=vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix().neq(b.getMatrix()))
    }
    throw FormulaParserException("Operation is not supported!")
}

internal fun RetVal.lt(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue().lt(b.getValue()))
        if (b.isVector()) return RetVal(getValue().lt(b.getVector()))
        if (b.isMatrix()) return RetVal(getValue().lt(b.getMatrix()))
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector().lt(b.getValue()))
        if (b.isVector()) return RetVal(getVector().lt(b.getVector()))
        if (b.isMatrix()) throw FormulaParserException("Operation vector<matrix is not supported!")
    }
    if (isMatrix()) {  // matrix
        if (b.isValue()) return RetVal(getMatrix().lt(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation matrix<vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix().lt(b.getMatrix()))
    }
    throw FormulaParserException("Operation is not supported!")
}

internal fun RetVal.gt(b: RetVal): RetVal {
    if (isValue()) {
        if (b.isValue()) return RetVal(getValue().gt(b.getValue()))
        if (b.isVector()) return RetVal(getValue().gt(b.getVector()))
        if (b.isMatrix()) return RetVal(getValue().gt(b.getMatrix()))
    }
    if (isVector()) {
        if (b.isValue()) return RetVal(getVector().gt(b.getValue()))
        if (b.isVector()) return RetVal(getVector().gt(b.getVector()))
        if (b.isMatrix()) throw FormulaParserException("Operation vector>matrix is not supported!")
    }
    if (isMatrix()) {  // matrix
        if (b.isValue()) return RetVal(getMatrix().gt(b.getValue()))
        if (b.isVector()) throw FormulaParserException("Operation matrix>vector is not supported!")
        if (b.isMatrix()) return RetVal(getMatrix().gt(b.getMatrix()))
    }
    throw FormulaParserException("Operation is not supported!")
}

internal fun RetVal.max(): RetVal {
    if (isValue()) return RetVal(getValue())
    if (isVector()) return RetVal(getVector().max()!!)
    if (isMatrix()) return RetVal(getMatrix().max())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}

internal fun RetVal.min(): RetVal {
    if (isValue()) return RetVal(getValue())
    if (isVector()) return RetVal(getVector().min()!!)
    if (isMatrix()) return RetVal(getMatrix().min())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}

internal fun RetVal.sum(): RetVal {
    if (isValue()) return RetVal(getValue())
    if (isVector()) return RetVal(getVector().sum())
    if (isMatrix()) return RetVal(getMatrix().sum())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}

internal fun RetVal.variance(): RetVal {
    if (isValue()) return RetVal(0.0)
    if (isVector()) return RetVal(getVector().variance())
    if (isMatrix()) return RetVal(getMatrix().variance())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}

internal fun RetVal.std(): RetVal {
    return RetVal((this.variance().getValue()).sqrt())
}

internal fun RetVal.mean(): RetVal {
    if (isValue()) return RetVal(getValue())
    if (isVector()) return RetVal(getVector().mean())
    if (isMatrix()) return RetVal(getMatrix().mean())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}

internal fun RetVal.median(): RetVal {
    if (isValue()) return RetVal(getValue())
    if (isVector()) return RetVal(getVector().median())
    if (isMatrix()) return RetVal(getMatrix().median())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}

internal fun RetVal.abs(): RetVal {
    if (isValue()) return RetVal(getValue().abs())
    if (isVector()) return RetVal(getVector().abs())
    if (isMatrix()) return RetVal(getMatrix().abs())
    throw FormulaParserException("Variables can be only scalars, vectors, or matrices!")
}