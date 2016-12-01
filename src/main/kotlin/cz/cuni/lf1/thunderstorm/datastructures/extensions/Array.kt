package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage

internal fun create2DDoubleArray(rows: Int, columns: Int, initValue: Double)
        = Array(rows) { Array(columns) { initValue } }

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