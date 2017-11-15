package cz.cuni.lf1.thunderstorm.algorithms.estimators

public class FittingGrid(fittingRadius: Int) {

    private val grid = (-fittingRadius..fittingRadius).map(Int::toDouble).toTypedArray()

    public fun getX(@Suppress("UNUSED_PARAMETER") row: Int, column: Int) = grid[column]
    public fun getY(row: Int, @Suppress("UNUSED_PARAMETER") column: Int) = grid[row]
}