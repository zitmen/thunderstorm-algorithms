package cz.cuni.lf1.thunderstorm.datastructures.extensions

import org.apache.commons.math3.util.FastMath

internal fun Double.abs()
        = FastMath.abs(this)

internal fun Double.ceil()
        = FastMath.ceil(this)

internal fun min(a: Int, b: Int)
        = FastMath.min(a, b)

internal fun max(a: Int, b: Int)
        = FastMath.max(a, b)

internal fun max(a: Double, b: Double)
        = FastMath.max(a, b)