package cz.cuni.lf1.thunderstorm.datastructures

public interface GrayScaleImage {

    public fun getWidth(): Int
    public fun getHeight(): Int
    public fun getValue(row: Int, column: Int): Double
}