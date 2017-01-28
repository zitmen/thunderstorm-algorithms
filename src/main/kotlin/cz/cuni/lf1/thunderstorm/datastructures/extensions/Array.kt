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

internal operator fun Array<Double>.div(by: Double)
        = this.map { it / by }.toTypedArray()

internal operator fun Array<Double>.plus(add: Double)
        = this.map { it + add }.toTypedArray()

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