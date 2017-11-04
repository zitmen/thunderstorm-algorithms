package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage

internal fun create2DDoubleArray(rows: Int, columns: Int, initValue: Double)
        = Array(rows) { Array(columns) { initValue } }

internal fun create2DDoubleArray(rows: Int, columns: Int, initFun: (row: Int, col: Int) -> Double)
        = Array(rows) { row -> Array(columns) { col -> initFun(row, col) } }

internal fun Array<Array<Double>>.copyDataToPosition(data: GrayScaleImage, row: Int, column: Int) {
    for ((dataY, y) in (row..min(row + data.getHeight() - 1, this.size - 1)).withIndex()) {
        for ((dataX, x) in (column..min(column + data.getHeight() - 1, this[y].size - 1)).withIndex()) {
            this[y][x] = data.getValue(dataY, dataX)
        }
    }
}

internal fun Array<Array<Double>>.fillRectangle(top: Int, left: Int, bottom: Int, right: Int, value: Double) {
    for (y in top..bottom) {
        for (x in left..right) {
            this[y][x] = value
        }
    }
}

internal operator fun Array<Double>.plus(add: Double)
        = this.map { it + add }.toTypedArray()

internal operator fun Double.plus(add: Array<Double>)
        = add + this

internal operator fun Array<Double>.plus(add: Array<Double>): Array<Double> {
    if (size != add.size) {
        throw IllegalArgumentException("Both arrays must be of the same size!")
    }
    return (0..size).map { this[it] + add[it] }.toTypedArray()
}

internal operator fun Array<Double>.minus(sub: Double)
        = this.map { it - sub }.toTypedArray()

internal operator fun Double.minus(sub: Array<Double>)
        = sub.map { this - it }.toTypedArray()

internal operator fun Array<Double>.minus(sub: Array<Double>): Array<Double> {
    if (size != sub.size) {
        throw IllegalArgumentException("Both arrays must be of the same size!")
    }
    return (0..size).map { this[it] - sub[it] }.toTypedArray()
}

internal operator fun Array<Double>.times(mul: Double)
        = this.map { it * mul }.toTypedArray()

internal operator fun Double.times(mul: Array<Double>)
        = mul * this

internal operator fun Array<Double>.times(mul: Array<Double>): Array<Double> {
    if (size != mul.size) {
        throw IllegalArgumentException("Both arrays must be of the same size!")
    }
    return (0..size).map { this[it] * mul[it] }.toTypedArray()
}

internal operator fun Array<Double>.div(by: Double)
        = this.map { it / by }.toTypedArray()

internal operator fun Double.div(by: Array<Double>)
        = by.map { this / it }.toTypedArray()

internal operator fun Array<Double>.div(by: Array<Double>): Array<Double> {
    if (size != by.size) {
        throw IllegalArgumentException("Both arrays must be of the same size!")
    }
    return (0..size).map { this[it] / by[it] }.toTypedArray()
}

internal operator fun Array<Double>.rem(by: Double)
        = this.map { it % by }.toTypedArray()

internal operator fun Double.rem(by: Array<Double>)
        = by.map { this % it }.toTypedArray()

internal operator fun Array<Double>.rem(by: Array<Double>): Array<Double> {
    if (size != by.size) {
        throw IllegalArgumentException("Both arrays must be of the same size!")
    }
    return (0..size).map { this[it] % by[it] }.toTypedArray()
}

internal fun Array<Double>.pow(to: Double)
        = this.map { it.pow(to) }.toTypedArray()

internal fun Array<Double>.and(arg: Array<Double>)
        = (0..size).map { this[it].and(arg[it]) }.toTypedArray()

internal fun Array<Double>.or(arg: Array<Double>)
        = (0..size).map { this[it].or(arg[it]) }.toTypedArray()

internal fun Array<Double>.eq(arg: Array<Double>)
        = (0..size).map { this[it].eq(arg[it]) }.toTypedArray()

internal fun Array<Double>.eq(arg: Double)
        = this.map { it.eq(arg) }.toTypedArray()

internal fun Double.eq(arg: Array<Double>)
        = arg.eq(this)

internal fun Array<Double>.neq(arg: Array<Double>)
        = (0..size).map { this[it].neq(arg[it]) }.toTypedArray()

internal fun Array<Double>.neq(arg: Double)
        = this.map { it.neq(arg) }.toTypedArray()

internal fun Double.neq(arg: Array<Double>)
        = arg.neq(this)

internal fun Array<Double>.lt(arg: Array<Double>)
        = (0..size).map { this[it].lt(arg[it]) }.toTypedArray()

internal fun Array<Double>.lt(arg: Double)
        = this.map { it.lt(arg) }.toTypedArray()

internal fun Double.lt(arg: Array<Double>)
        = arg.lt(this)

internal fun Array<Double>.gt(arg: Array<Double>)
        = (0..size).map { this[it].gt(arg[it]) }.toTypedArray()

internal fun Array<Double>.gt(arg: Double)
        = this.map { it.gt(arg) }.toTypedArray()

internal fun Double.gt(arg: Array<Double>)
        = arg.gt(this)

internal fun Array<Double>.median()
        = sortedArray().let {
            if (size % 2 == 1) {
                it[it.size / 2]
            } else {
                (it[it.size / 2] + it[(it.size - 1) / 2]) / 2.0
            }
        }

/**
 * Normalize sum to 1
 */
internal fun Array<Double>.normalize()
        = div(sum())

internal fun Array<Double>.mean()
    = this.sum() / this.size

internal fun Array<Double>.variance(): Double {
    val avg = this.mean()
    return this.map { (it - avg).sqr() }.toTypedArray().mean()
}

internal fun Array<Double>.abs()
    = this.map { it.abs() }.toTypedArray()