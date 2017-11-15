package cz.cuni.lf1.thunderstorm.datastructures.extensions

import org.apache.commons.math3.util.FastMath

internal fun Double.abs()
        = FastMath.abs(this)

internal fun Double.round()
        = FastMath.round(this)

internal fun Double.roundRem()
        = this - FastMath.round(this).toDouble()

internal fun Double.ceil()
        = FastMath.ceil(this)

internal fun min(a: Int, b: Int)
        = FastMath.min(a, b)

internal fun max(a: Int, b: Int)
        = FastMath.max(a, b)

internal fun max(a: Double, b: Double)
        = FastMath.max(a, b)

internal fun Double.sqr()
        = this * this

internal fun Double.sqrt()
        = FastMath.sqrt(this)

internal fun Int.pow(to: Int)
        = FastMath.pow(this.toDouble(), to).toInt()

internal fun Double.pow(to: Double)
        = FastMath.pow(this, to)

internal fun Double.exp()
        = FastMath.exp(this)

internal fun gauss(x: Double, sigma: Double)
        = (-0.5 * (x / sigma).sqr()).exp()

internal fun Double.and(x: Double)
        = if ((this != 0.0) && (x != 0.0)) 1.0 else 0.0

internal fun Double.or(x: Double)
        = if ((this != 0.0) || (x != 0.0)) 1.0 else 0.0

internal fun Double.eq(x: Double)
        = if (this == x) 1.0 else 0.0

internal fun Double.neq(x: Double)
        = if (this != x) 1.0 else 0.0

internal fun Double.lt(x: Double)
        = if (this < x) 1.0 else 0.0

internal fun Double.gt(x: Double)
        = if (this > x) 1.0 else 0.0