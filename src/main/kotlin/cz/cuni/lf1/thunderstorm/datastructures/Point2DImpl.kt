package cz.cuni.lf1.thunderstorm.datastructures

internal class Point2DImpl(
        private val x: Double,
        private val y: Double) : Point2D {

    public override fun getX(): Double
            = x

    public override fun getY(): Double
            = y
}