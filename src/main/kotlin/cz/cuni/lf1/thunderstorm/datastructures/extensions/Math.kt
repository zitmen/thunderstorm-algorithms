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

internal fun Double.sqr()
        = this * this

internal fun Int.pow(to: Int)
        = FastMath.pow(this.toDouble(), to).toInt()

internal fun Double.exp()
        = FastMath.exp(this)

internal fun gauss(x: Double, sigma: Double)
        = (-0.5 * (x / sigma).sqr()).exp()